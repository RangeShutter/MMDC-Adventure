-- MotorPH Payroll System - M2: Semi-monthly withholding tax brackets
-- Converted from monthly MotorPH/BIR table (document/Witholding Tax.md)
-- Rules: Min/Max/Base/ExcessOver divided by 2; ExcessRate unchanged

USE payrollsystem_db;

DROP TABLE IF EXISTS WithholdingTaxBracketSemiMonthly;

CREATE TABLE WithholdingTaxBracketSemiMonthly (
    BracketID INT NOT NULL AUTO_INCREMENT,
    SemiMonthlyRateMin DECIMAL(12, 2) NOT NULL,
    SemiMonthlyRateMax DECIMAL(12, 2) NULL,
    TaxRuleDescription VARCHAR(255) NOT NULL,
    BaseTax DECIMAL(12, 2) NOT NULL DEFAULT 0,
    ExcessRate DECIMAL(5, 4) NULL,
    ExcessOver DECIMAL(12, 2) NULL,
    PRIMARY KEY (BracketID),
    CONSTRAINT chk_semi_withholding_base CHECK (BaseTax >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Monthly source -> semi-monthly (Option A payroll; tax brackets halved per M2 spec)
INSERT INTO WithholdingTaxBracketSemiMonthly
    (SemiMonthlyRateMin, SemiMonthlyRateMax, TaxRuleDescription, BaseTax, ExcessRate, ExcessOver)
VALUES
(0.00, 10416.00,
 '10,416 and below - No withholding tax (from monthly 20,832)',
 0.00, NULL, NULL),
(10417.00, 16666.00,
 '10,417 to 16,666 - 20% in excess of 10,417 (from monthly 20,833-33,332)',
 0.00, 0.2000, 10417.00),
(16667.00, 33333.00,
 '16,667 to 33,333 - 1,250 plus 25% in excess of 16,667 (from monthly 33,333-66,666)',
 1250.00, 0.2500, 16667.00),
(33334.00, 83333.00,
 '33,334 to 83,333 - 5,416.50 plus 30% in excess of 33,334 (from monthly 66,667-166,666)',
 5416.50, 0.3000, 33334.00),
(83334.00, 333333.00,
 '83,334 to 333,333 - 20,416.67 plus 32% in excess of 83,334 (from monthly 166,667-666,666)',
 20416.67, 0.3200, 83334.00),
(333334.00, NULL,
 '333,334 and above - 100,416.67 plus 35% in excess of 333,334 (from monthly 666,667+)',
 100416.67, 0.3500, 333334.00);
