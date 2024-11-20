-- MAME.VW_SGQ_CICLI_CAR_TMP_FORM source

CREATE VIEW MAME.VW_SGQ_CICLI_CAR_TMP_FORM AS
SELECT
    SGQ_CICLI_CAR.ID_AZIENDA,
    SGQ_CICLI_CAR.ID_PROGRESSIVO,
    SGQ_CICLI_CAR.ID_SEQUENZA_FASE,
    SGQ_CICLI_CAR.ID_SEQUENZA_CAR,
    SGQ_CICLI_CAR.DESCRIZIONE,
    SGQ_CICLI_CAR.DESCR_RIDOTTA,
    SGQ_CICLI_CAR.SEQ_VISUALIZZ,
    SGQ_CICLI_CAR.TIPO_CAR,
    SGQ_CICLI_CAR.CLASSE_CAR,
    SGQ_CICLI_CAR.CTR_OBBL,
    SGQ_CICLI_CAR.R_MASTER_TIP_RIS,
    SGQ_CICLI_CAR.R_MASTER_LIV_RIS,
    SGQ_CICLI_CAR.R_MASTER_RIS,
    SGQ_CICLI_CAR.R_STRUM_TIP_RIS1,
    SGQ_CICLI_CAR.R_STRUM_LIV_RIS1,
    SGQ_CICLI_CAR.R_STRUM_RIS1,
    SGQ_CICLI_CAR.R_STRUM_TIP_RIS2,
    SGQ_CICLI_CAR.R_STRUM_LIV_RIS2,
    SGQ_CICLI_CAR.R_STRUM_RIS2,
    SGQ_CICLI_CAR.VALORE_NOMINALE,
    SGQ_CICLI_CAR.R_UNITA_MISURA,
    SGQ_CICLI_CAR.LIV_ISPEZIONE,
    SGQ_CICLI_CAR.COEF_LQA,
    CASE
        WHEN SGQ_CICLI_CAR.LIM_SUP_TOL IN (- 9999.00000,
                                                9999.00000) THEN NULL
        ELSE SGQ_CICLI_CAR.LIM_SUP_TOL
    END AS LIM_SUP_TOL,
    CASE
        WHEN SGQ_CICLI_CAR.LIM_INF_TOL IN (- 9999.00000,
                                                9999.00000) THEN NULL
        ELSE SGQ_CICLI_CAR.LIM_INF_TOL
    END AS LIM_INF_TOL,
    SGQ_CICLI_CAR.LIM_SUP_DEROGA,
    SGQ_CICLI_CAR.LIM_INF_DEROGA,
    SGQ_CICLI_CAR.LIM_SUP_ACC,
    SGQ_CICLI_CAR.LIM_INF_ACC,
    SGQ_CICLI_CAR.RILAVORAZIONE,
    SGQ_CICLI_CAR.QTA_CTR,
    SGQ_CICLI_CAR.PERC_CTR,
    SGQ_CICLI_CAR.NUM_DIF_ACC,
    SGQ_CICLI_CAR.NUM_DIF_RIF,
    SGQ_CICLI_CAR.TIPO_CARTA_CTR,
    SGQ_CICLI_CAR.RIL_LIM_SUP_TOL,
    SGQ_CICLI_CAR.RIL_LIM_INF_TOL,
    SGQ_CICLI_CAR.NOTE,
    SGQ_CICLI_CAR.STATO,
    SGQ_CICLI_CAR.R_UTENTE_CRZ,
    SGQ_CICLI_CAR.R_UTENTE_AGG,
    SGQ_CICLI_CAR.TIMESTAMP_CRZ,
    SGQ_CICLI_CAR.TIMESTAMP_AGG,
    SGQ_CICLI_CAR.R_P_REAZIONE,
    SGQ_CICLI_CAR.DEF_MASTER_TIP_RIS,
    SGQ_CICLI_CAR.DEF_MASTER_LIV_RIS,
    SGQ_CICLI_CAR.DEF_MASTER_RIS,
    SGQ_CICLI_CAR.DEF_STRUM_TIP_RIS1,
    SGQ_CICLI_CAR.DEF_STRUM_LIV_RIS1,
    SGQ_CICLI_CAR.DEF_STRUM_RIS1,
    SGQ_CICLI_CAR.DEF_STRUM_TIP_RIS2,
    SGQ_CICLI_CAR.DEF_STRUM_LIV_RIS2,
    SGQ_CICLI_CAR.DEF_STRUM_RIS2,
    SGQ_CICLI_CAR.VARIAZ_PZ_CTR,
    CASE
        WHEN SGQ_CICLI_CAR_MAME.YINFORMATIVO = 'N' THEN 0
        ELSE 1
    END AS INF,
    SGQ_CICLI_CAR_MAME.YFORMULA,
    ROW_NUMBER() OVER (PARTITION BY SGQ_CICLI_CAR_MAME.ID_AZIENDA,
    SGQ_CICLI_CAR_MAME.ID_PROGRESSIVO,
    SGQ_CICLI_CAR_MAME.ID_SEQUENZA_FASE
ORDER BY
    SGQ_CICLI_CAR_MAME.ID_AZIENDA,
    SGQ_CICLI_CAR_MAME.ID_PROGRESSIVO,
    SGQ_CICLI_CAR_MAME.ID_SEQUENZA_FASE) AS ENUM
FROM
    THIP.SGQ_CICLI_CAR AS SGQ_CICLI_CAR
INNER JOIN MAME.SGQ_CICLI_CAR_MAME AS SGQ_CICLI_CAR_MAME ON
    SGQ_CICLI_CAR_MAME.ID_AZIENDA = SGQ_CICLI_CAR.ID_AZIENDA
    AND SGQ_CICLI_CAR_MAME.ID_PROGRESSIVO = SGQ_CICLI_CAR.ID_PROGRESSIVO
    AND SGQ_CICLI_CAR_MAME.ID_SEQUENZA_FASE = SGQ_CICLI_CAR.ID_SEQUENZA_FASE
    AND SGQ_CICLI_CAR_MAME.ID_SEQUENZA_CAR = SGQ_CICLI_CAR.ID_SEQUENZA_CAR
WHERE
    SGQ_CICLI_CAR.DESCR_RIDOTTA NOT IN ('Al',
                                          'Al Sol',
                                          'As',
                                          'B',
                                          'Bi',
                                          'C',
                                          'Ca',
                                          'Cb',
                                          'Ceq',
                                          'Co',
                                          'Cr',
                                          'Creq',
                                          'Cu',
                                          'H',
                                          'J Fact',
                                          'Mn',
                                          'Mo',
                                          'N',
                                          'Nb',
                                          'Ni',
                                          'O',
                                          'P',
                                          'Pb',
                                          'PCM',
                                          'Pre',
                                          'S',
                                          'Sb',
                                          'Si',
                                          'Sn',
                                          'Ta',
                                          'Ti',
                                          'V',
                                          'W',
                                          'X Fact',
                                          'Zr',
                                          'C + N',
                                          'Fe',
                                          'Nb + Ta',
                                          'Y')
    AND YFORMULA IS NOT NULL;