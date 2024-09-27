/*
 * @(#)YMisuraCaracteriche.java
 */

/**
 * null
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
import java.sql.*;
import java.util.*;
import it.thera.thip.qualita.controllo.*;
import com.thera.thermfw.common.*;
import it.thera.thip.base.azienda.Azienda;

public class YMisuraCaracteriche extends MisuraCaracteriche {

  
  /**
   * Attributo iYFormula
   */
  protected String iYFormula;

  
  /**
   * YMisuraCaracteriche
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 26/09/2024    Wizard     Codice generato da Wizard
   *
   */
  public YMisuraCaracteriche() {
    setIdAzienda(Azienda.getAziendaCorrente());
  }

  /**
   * Valorizza l'attributo. 
   * @param yFormula
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 26/09/2024    Wizard     Codice generato da Wizard
   *
   */
  public void setYFormula(String yFormula) {
    this.iYFormula = yFormula;
    setDirty();
  }

  /**
   * Restituisce l'attributo. 
   * @return String
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 26/09/2024    Wizard     Codice generato da Wizard
   *
   */
  public String getYFormula() {
    return iYFormula;
  }

  /**
   * setEqual
   * @param obj
   * @throws CopyException
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 26/09/2024    Wizard     Codice generato da Wizard
   *
   */
  public void setEqual(Copyable obj) throws CopyException {
    super.setEqual(obj);
  }

}

