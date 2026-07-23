package com.dukaniledger.controller;

import com.dukaniledger.dto.DailyProfitResponse;
import com.dukaniledger.dto.MonthlyProfitResponse;
import com.dukaniledger.dto.ProductProfitResponse;
import com.dukaniledger.service.ProfitService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/profits")
@RequiredArgsConstructor
public class ProfitController {

    private final ProfitService profitService;

    // Lifetime profit per product, highest profit first.
    @GetMapping("/products")
    public List<ProductProfitResponse> getProfitByProduct(){
        return profitService.getProfitByProduct();
    }

    // ?date=2026-07-23 - defaults to today if omitted.
    @GetMapping("/daily")
    public DailyProfitResponse getDailyProfit(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ){
        return profitService.getDailyProfit(date);
    }

    // ?start=2026-07-01&end=2026-07-23 - defaults to the last 7 days if omitted.
    @GetMapping("/daily/history")
    public List<DailyProfitResponse> getDailyProfitHistory(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ){
        return profitService.getDailyProfitHistory(start, end);
    }

    // ?year=2026&month=7 - defaults to the current year/month if omitted.
    @GetMapping("/monthly")
    public MonthlyProfitResponse getMonthlyProfit(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month
    ){
        return profitService.getMonthlyProfit(year, month);
    }

    // ?year=2026 - all 12 months, defaults to the current year if omitted.
    @GetMapping("/monthly/history")
    public List<MonthlyProfitResponse> getMonthlyProfitHistory(
            @RequestParam(required = false) Integer year
    ){
        return profitService.getMonthlyProfitHistory(year);
    }
}