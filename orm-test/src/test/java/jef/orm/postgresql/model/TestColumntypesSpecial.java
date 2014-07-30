package jef.orm.postgresql.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import jef.codegen.support.NotModified;
import jef.database.DataObject;
/**
 * This class was generated by EasyFrame according to the table in database.
 * You need to modify the type of primary key field, to the strategy your own.
 */
@NotModified
@Entity
@Table(name="test_columntypes_special")
public class TestColumntypesSpecial extends DataObject{


	@Column(name="moneyfield",columnDefinition="Varchar",length=2147483647)
	private String moneyfield;

	@Column(name="varbitfield1",columnDefinition="Varchar",length=63)
	private String varbitfield1;

	@Column(name="varbitfield2",columnDefinition="Varchar",length=2147483647)
	private String varbitfield2;

	@Column(name="bitfield1",columnDefinition="Char",length=8)
	private String bitfield1;

	@Column(name="bitfield2",columnDefinition="Char",length=1)
	private String bitfield2;

	@Column(name="intervalfield",columnDefinition="Varchar",length=49)
	private String intervalfield;

	@Column(name="cidrfield",columnDefinition="Varchar",length=2147483647)
	private String cidrfield;

	@Column(name="inetfield",columnDefinition="Varchar",length=2147483647)
	private String inetfield;

	@Column(name="macaddrfield",columnDefinition="Varchar",length=2147483647)
	private String macaddrfield;

	@Column(name="uuidfield",columnDefinition="Varchar",length=2147483647)
	private String uuidfield;

	@Column(name="tsvectorfield",columnDefinition="Varchar",length=2147483647)
	private String tsvectorfield;

	@Column(name="tsqueryfield",columnDefinition="Varchar",length=2147483647)
	private String tsqueryfield;

	@Lob
	@Column(name="xmlfield",columnDefinition="Clob")
	private String xmlfield;

	@Column(name="txidfield",columnDefinition="Varchar",length=2147483647)
	private String txidfield;

	@Column(name="boxfield",columnDefinition="Varchar",length=2147483647)
	private String boxfield;

	@Column(name="circlefield",columnDefinition="Varchar",length=2147483647)
	private String circlefield;

	@Column(name="linefield",columnDefinition="Varchar",length=2147483647)
	private String linefield;

	@Column(name="lsegfield",columnDefinition="Varchar",length=2147483647)
	private String lsegfield;

	@Column(name="pathfield",columnDefinition="Varchar",length=2147483647)
	private String pathfield;

	@Column(name="pointfield",columnDefinition="Varchar",length=2147483647)
	private String pointfield;

	@Column(name="polygonfield",columnDefinition="Varchar",length=2147483647)
	private String polygonfield;

	public void setMoneyfield(String obj){
		this.moneyfield = obj;
	}

	public String getMoneyfield(){
		return moneyfield;
	}

	public void setVarbitfield1(String obj){
		this.varbitfield1 = obj;
	}

	public String getVarbitfield1(){
		return varbitfield1;
	}

	public void setVarbitfield2(String obj){
		this.varbitfield2 = obj;
	}

	public String getVarbitfield2(){
		return varbitfield2;
	}

	public void setBitfield1(String obj){
		this.bitfield1 = obj;
	}

	public String getBitfield1(){
		return bitfield1;
	}

	public void setBitfield2(String obj){
		this.bitfield2 = obj;
	}

	public String getBitfield2(){
		return bitfield2;
	}

	public void setIntervalfield(String obj){
		this.intervalfield = obj;
	}

	public String getIntervalfield(){
		return intervalfield;
	}

	public void setCidrfield(String obj){
		this.cidrfield = obj;
	}

	public String getCidrfield(){
		return cidrfield;
	}

	public void setInetfield(String obj){
		this.inetfield = obj;
	}

	public String getInetfield(){
		return inetfield;
	}

	public void setMacaddrfield(String obj){
		this.macaddrfield = obj;
	}

	public String getMacaddrfield(){
		return macaddrfield;
	}

	public void setUuidfield(String obj){
		this.uuidfield = obj;
	}

	public String getUuidfield(){
		return uuidfield;
	}

	public void setTsvectorfield(String obj){
		this.tsvectorfield = obj;
	}

	public String getTsvectorfield(){
		return tsvectorfield;
	}

	public void setTsqueryfield(String obj){
		this.tsqueryfield = obj;
	}

	public String getTsqueryfield(){
		return tsqueryfield;
	}

	public void setXmlfield(String obj){
		this.xmlfield = obj;
	}

	public String getXmlfield(){
		return xmlfield;
	}

	public void setTxidfield(String obj){
		this.txidfield = obj;
	}

	public String getTxidfield(){
		return txidfield;
	}

	public void setBoxfield(String obj){
		this.boxfield = obj;
	}

	public String getBoxfield(){
		return boxfield;
	}

	public void setCirclefield(String obj){
		this.circlefield = obj;
	}

	public String getCirclefield(){
		return circlefield;
	}

	public void setLinefield(String obj){
		this.linefield = obj;
	}

	public String getLinefield(){
		return linefield;
	}

	public void setLsegfield(String obj){
		this.lsegfield = obj;
	}

	public String getLsegfield(){
		return lsegfield;
	}

	public void setPathfield(String obj){
		this.pathfield = obj;
	}

	public String getPathfield(){
		return pathfield;
	}

	public void setPointfield(String obj){
		this.pointfield = obj;
	}

	public String getPointfield(){
		return pointfield;
	}

	public void setPolygonfield(String obj){
		this.polygonfield = obj;
	}

	public String getPolygonfield(){
		return polygonfield;
	}

	public TestColumntypesSpecial(){
	}


public enum Field implements jef.database.Field{moneyfield,varbitfield1,varbitfield2,bitfield1,bitfield2,intervalfield,cidrfield,inetfield,macaddrfield,uuidfield,tsvectorfield,tsqueryfield,xmlfield,txidfield,boxfield,circlefield,linefield,lsegfield,pathfield,pointfield,polygonfield}
}
