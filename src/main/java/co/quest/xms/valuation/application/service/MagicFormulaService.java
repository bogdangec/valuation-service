package co.quest.xms.valuation.application.service;

import co.quest.xms.valuation.api.dto.MagicFormulaResult;

/**
 * Port interface for calculating magic formula.
 */
public interface MagicFormulaService {
    /**
     * Calculate Magic Formula rankings for a given stock symbol.
     *
     * @param symbol The stock symbol.
     * @return MagicFormulaResult containing rankings and metrics.
     */
    MagicFormulaResult calculateMagicFormula(String symbol);
}
