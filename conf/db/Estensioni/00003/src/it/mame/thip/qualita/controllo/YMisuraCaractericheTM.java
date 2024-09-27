/*
 * @(#)YMisuraCaractericheTM.java
 */

/**
 * YMisuraCaractericheTM
 *
 * <br></br><b>Copyright (C) : Thera SpA</b>
 * @author Wizard 26/09/2024 at 11:41:20
 */
/*
 * Revisions:
 * Date          Owner      Description
 * 26/09/2024    Wizard     Codice generato da Wizard
 *
 */
package it.mame.thip.qualita.controllo;
import com.thera.thermfw.persist.*;
import com.thera.thermfw.common.*;
import java.sql.*;
import it.thera.thip.qualita.controllo.*;
import com.thera.thermfw.base.*;

public class YMisuraCaractericheTM extends MisuraCaractericheTM {

  
  /**
   * Attributo YFORMULA
   */
  public static final String YFORMULA = "YFORMULA";

  /**
   *  TABLE_NAME
   */
  public static final String TABLE_NAME_EXT = SystemParam.getSchema("THIPPERS") + "YSGQ_MIS_CAR";

  /**
   *  CLASS_NAME
   */
  private static final String CLASS_NAME = it.mame.thip.qualita.controllo.YMisuraCaracteriche.class.getName();

  
  /**
   *  YMisuraCaractericheTM
   * @throws SQLException
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 26/09/2024    CodeGen     Codice generato da CodeGenerator
   *
   */
  public YMisuraCaractericheTM() throws SQLException {
    super();
  }

  /**
   *  initialize
   * @throws SQLException
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 26/09/2024    CodeGen     Codice generato da CodeGenerator
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
   * 26/09/2024    Wizard     Codice generato da Wizard
   *
   */
  protected void initializeRelation() throws SQLException {
    super.initializeRelation();
    linkTable(TABLE_NAME_EXT);
    addAttributeOnTable("YFormula", YFORMULA, TABLE_NAME_EXT);
    
    

  }

}

