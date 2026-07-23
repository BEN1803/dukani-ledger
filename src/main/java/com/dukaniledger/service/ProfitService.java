package com.dukaniledger.service;

import com.dukaniledger.dto.DailyProfitResponse;
import com.dukaniledger.dto.MonthlyProfitResponse;
import com.dukaniledger.dto.ProductProfitResponse;
import com.dukaniledger.repository.DailyProfitProjection;
import com.dukaniledger.repository.MonthlyProfitProjection;
import com.dukaniledger.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfitService {

    private final SaleRepository saleRepository;
    private final BusinessContextService businessContextService;

    // Lifetime profit per product for this shop. Owner/admin only - workers
    // can already see cost + selling price on a product, but margin summed
    // across every sale is a step further than that and worth gating.
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")
    @Cacheable(value = "profits", key = "'products:' + #root.target.currentOwnerId()")
    public List<ProductProfitResponse> getProfitByProduct(){
        Long ownerId = currentOwnerId();
        return saleRepository.findProfitByProduct(ownerId)
                .stream()
                .map(p -> ProductProfitResponse.builder()
                        .productId(p.getProductId())
                        .productName(p.getProductName())
                        .quantitySold(p.getQuantitySold())
                        .totalRevenue(p.getTotalRevenue())
                        .totalCost(p.getTotalCost())
                        .totalProfit(p.getTotalProfit())
                        .build())
                .collect(Collectors.toList());
    }

    // Profit for one specific day - defaults to today if no date is passed.
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")
    public DailyProfitResponse getDailyProfit(LocalDate date){
        LocalDate targetDate = date != null ? date : LocalDate.now();
        BigDecimal profit = saleRepository.findProfitForDate(currentOwnerId(), targetDate);

        return DailyProfitResponse.builder()
                .date(targetDate)
                .totalProfit(profit != null ? profit : BigDecimal.ZERO)
                .build();
    }

    // Day-by-day trend. Defaults to the last 7 days (inclusive) if no range given.
    // Fills in zero-profit days so callers get a continuous series for charting.
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")
    public List<DailyProfitResponse> getDailyProfitHistory(LocalDate start, LocalDate end){
        LocalDate rangeEnd = end != null ? end : LocalDate.now();
        LocalDate rangeStart = start != null ? start : rangeEnd.minusDays(6);

        Map<LocalDate, BigDecimal> byDate = saleRepository
                .findDailyProfitBetween(currentOwnerId(), rangeStart, rangeEnd.plusDays(1))
                .stream()
                .collect(Collectors.toMap(
                        DailyProfitProjection::getSaleDate,
                        DailyProfitProjection::getTotalProfit
                ));

        List<DailyProfitResponse> result = new ArrayList<>();
        for (LocalDate d = rangeStart; !d.isAfter(rangeEnd); d = d.plusDays(1)) {
            result.add(DailyProfitResponse.builder()
                    .date(d)
                    .totalProfit(byDate.getOrDefault(d, BigDecimal.ZERO))
                    .build());
        }
        return result;
    }

    // Profit for one specific month - defaults to the current month if not given.
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")
    public MonthlyProfitResponse getMonthlyProfit(Integer year, Integer month){
        LocalDate now = LocalDate.now();
        int targetYear = year != null ? year : now.getYear();
        int targetMonth = month != null ? month : now.getMonthValue();

        BigDecimal profit = saleRepository.findProfitForMonth(currentOwnerId(), targetYear, targetMonth);

        return MonthlyProfitResponse.builder()
                .year(targetYear)
                .month(targetMonth)
                .totalProfit(profit != null ? profit : BigDecimal.ZERO)
                .build();
    }

    // Month-by-month trend for one calendar year, all 12 months filled in.
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")
    @Cacheable(value = "profits", key = "'monthly:' + #root.target.currentOwnerId() + ':' + #p0")
    public List<MonthlyProfitResponse> getMonthlyProfitHistory(Integer year){
        int targetYear = year != null ? year : LocalDate.now().getYear();
        Long ownerId = currentOwnerId();

        Map<Integer, BigDecimal> byMonth = saleRepository
                .findMonthlyProfitForYear(ownerId, targetYear)
                .stream()
                .collect(Collectors.toMap(
                        MonthlyProfitProjection::getSaleMonth,
                        MonthlyProfitProjection::getTotalProfit
                ));

        List<MonthlyProfitResponse> result = new ArrayList<>();
        for (int m = 1; m <= 12; m++) {
            result.add(MonthlyProfitResponse.builder()
                    .year(targetYear)
                    .month(m)
                    .totalProfit(byMonth.getOrDefault(m, BigDecimal.ZERO))
                    .build());
        }
        return result;
    }

    // Used by the @Cacheable keys above to scope caching per shop.
    public Long currentOwnerId(){
        return businessContextService.getOwnerForCurrentUser().getId();
    }
}