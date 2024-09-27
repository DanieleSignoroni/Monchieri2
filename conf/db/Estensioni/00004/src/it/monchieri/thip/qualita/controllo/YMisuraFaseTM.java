/*
 * @(#)YMisuraFaseTM.java
 */

/**
 * YMisuraFaseTM
 *
 * <br></br><b>Copyright (C) : Thera SpA</b>
 * @author Wizard 27/09/2024 at 16:10:13
 */
/*
 * Revisions:
 * Date          Owner      Description
 * 27/09/2024    Wizard     Codice generato da Wizard
 *
 */
package it.monchieri.thip.qualita.controllo;
import com.thera.thermfw.persist.*;
import com.thera.thermfw.common.*;
import java.sql.*;
import it.thera.thip.qualita.controllo.*;
import com.thera.thermfw.base.*;

public class YMisuraFaseTM extends MisuraFaseTM {

  
  /**
   * Attributo R_FASE_MAME
   */
  public static final String R_FASE_MAME = "R_FASE_MAME";

  /**
   *  TABLE_NAME
   */
  public static final String TABLE_NAME_EXT = SystemParam.getSchema("THIPPERS") + "YSGQ_MIS_FAS";

  /**
   *  CLASS_NAME
   */
  private static final String CLASS_NAME = it.monchieri.thip.qualita.controllo.YMisuraFase.class.getName();

  
  /**
   *  YMisuraFaseTM
   * @throws SQLException
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 27/09/2024    CodeGen     Codice generato da CodeGenerator
   *
   */
  public YMisuraFaseTM() throws SQLException {
    super();
  }

  /**
   *  initialize
   * @throws SQLException
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 27/09/2024    CodeGen     Codice generato da CodeGenerator
   *
   */
  protected void initialize() throws SQLException {
    super.initialize();
    setObjClassName(CLASS_NAME);
  }

  /**
   *  initializeRelation
   * @throws SQLException
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 27/09/2024    Wizard     Codice generato da Wizard
   *
   */
  protected void initializeRelation() throws SQLException {
    super.initializeRelation();
    linkTable(TABLE_NAME_EXT);
    addAttributeOnTable("IdCodificaFaseMame", R_FASE_MAME, "getShortObject", TABLE_NAME_EXT);
    
    

  }

}

