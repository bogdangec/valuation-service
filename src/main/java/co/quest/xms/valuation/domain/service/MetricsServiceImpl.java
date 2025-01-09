package co.quest.xms.valuation.domain.service;

import co.quest.xms.valuation.application.service.MetricsService;
import co.quest.xms.valuation.domain.model.FinancialMetrics;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_EVEN;

@Service
@RequiredArgsConstructor
public class MetricsServiceImpl implements MetricsService {

    @Override
    public double calculateEarningsYield(FinancialMetrics metrics) {
        if (metrics.getEbit() == null || metrics.getMarketCapitalization() == null || metrics.getTotalDebt() == null || metrics.getCash() == null) {
            throw new IllegalArgumentException("Invalid financial data for Earnings Yield calculation");
        }

        double enterpriseValue = metrics.getMarketCapitalization() + metrics.getTotalDebt() - metrics.getCash();
        if (enterpriseValue <= 0) {
            throw new IllegalArgumentException("Enterprise Value must be greater than zero");
        }

        return new BigDecimal(metrics.getEbit() / enterpriseValue).setScale(4, HALF_EVEN).doubleValue();
    }

    @Override
    public double calculateReturnOnCapital(FinancialMetrics metrics) {
        if (metrics.getEbit() == null || metrics.getNetWorkingCapital() == null || metrics.getNetFixedAssets() == null) {
            throw new IllegalArgumentException("Invalid financial data for ROC calculation");
        }

        double denominator = metrics.getNetWorkingCapital() + metrics.getNetFixedAssets();
        if (denominator <= 0) {
            throw new IllegalArgumentException("Denominator for ROC calculation must be greater than zero");
        }

        return new BigDecimal(metrics.getEbit() / denominator).setScale(4, HALF_EVEN).doubleValue();
    }
}
