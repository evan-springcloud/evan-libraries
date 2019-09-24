package test.org.evan.libraries.orm.support.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * 示例
 */
public class Demo implements Serializable {

    private static final long serialVersionUID = 13792681968342L;

    private Long id;

    private Date gmtCreate;

    private Date gmtModify;

    private Integer status;

    private Date fieldDate;

    private Date fieldDatetime;

    private String fieldSelect;

    private String fieldRadio;

    private String fieldCheckbox;

    private BigDecimal fieldNumber;

    private String fieldText;

    private String fieldProvince;

    private String fieldCity;

    private String fieldRegion;

    private String imagePath;

    private String imagePathList;

    private String fieldTextarea;

    private String fieldHtmleditor;

    private String fieldHtmleditorCut;

    public Demo() {
    }

    public Demo(Long id) {
        this.id = id;
    }

    /**
     *
     */
    public Long getId() {
        return id;
    }

    /***/
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /***/
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     *
     */
    public Date getGmtModify() {
        return gmtModify;
    }

    /***/
    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }

    /**
     *
     */
    public Integer getStatus() {
        return status;
    }

    /***/
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     *
     */
    public Date getFieldDate() {
        return fieldDate;
    }

    /***/
    public void setFieldDate(Date fieldDate) {
        this.fieldDate = fieldDate;
    }

    /**
     *
     */
    public Date getFieldDatetime() {
        return fieldDatetime;
    }

    /***/
    public void setFieldDatetime(Date fieldDatetime) {
        this.fieldDatetime = fieldDatetime;
    }

    /**
     *
     */
    public String getFieldSelect() {
        return fieldSelect;
    }

    /***/
    public void setFieldSelect(String fieldSelect) {
        this.fieldSelect = fieldSelect;
    }

    /**
     *
     */
    public String getFieldRadio() {
        return fieldRadio;
    }

    /***/
    public void setFieldRadio(String fieldRadio) {
        this.fieldRadio = fieldRadio;
    }

    /**
     *
     */
    public String getFieldCheckbox() {
        return fieldCheckbox;
    }

    /***/
    public void setFieldCheckbox(String fieldCheckbox) {
        this.fieldCheckbox = fieldCheckbox;
    }

    /**
     *
     */
    public BigDecimal getFieldNumber() {
        return fieldNumber;
    }

    /***/
    public void setFieldNumber(BigDecimal fieldNumber) {
        this.fieldNumber = fieldNumber;
    }

    /**
     *
     */
    public String getFieldText() {
        return fieldText;
    }

    /***/
    public void setFieldText(String fieldText) {
        this.fieldText = fieldText;
    }

    /**
     *
     */
    public String getFieldProvince() {
        return fieldProvince;
    }

    /***/
    public void setFieldProvince(String fieldProvince) {
        this.fieldProvince = fieldProvince;
    }

    /**
     *
     */
    public String getFieldCity() {
        return fieldCity;
    }

    /***/
    public void setFieldCity(String fieldCity) {
        this.fieldCity = fieldCity;
    }

    /**
     *
     */
    public String getFieldRegion() {
        return fieldRegion;
    }

    /***/
    public void setFieldRegion(String fieldRegion) {
        this.fieldRegion = fieldRegion;
    }

    /**
     *
     */
    public String getImagePath() {
        return imagePath;
    }

    /***/
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     *
     */
    public String getImagePathList() {
        return imagePathList;
    }

    /***/
    public void setImagePathList(String imagePathList) {
        this.imagePathList = imagePathList;
    }

    /**
     *
     */
    public String getFieldTextarea() {
        return fieldTextarea;
    }

    /***/
    public void setFieldTextarea(String fieldTextarea) {
        this.fieldTextarea = fieldTextarea;
    }

    /**
     *
     */
    public String getFieldHtmleditor() {
        return fieldHtmleditor;
    }

    /***/
    public void setFieldHtmleditor(String fieldHtmleditor) {
        this.fieldHtmleditor = fieldHtmleditor;
    }

    /**
     *
     */
    public String getFieldHtmleditorCut() {
        return fieldHtmleditorCut;
    }

    /***/
    public void setFieldHtmleditorCut(String fieldHtmleditorCut) {
        this.fieldHtmleditorCut = fieldHtmleditorCut;
    }

    

    @Override
    public String toString() {
        return "Demo{" +
                "id=" + id +
                ", gmtCreate=" + gmtCreate +
                ", gmtModify=" + gmtModify +
                ", status=" + status +
                ", fieldDate=" + fieldDate +
                ", fieldDatetime=" + fieldDatetime +
                ", fieldSelect='" + fieldSelect + '\'' +
                ", fieldRadio='" + fieldRadio + '\'' +
                ", fieldCheckbox='" + fieldCheckbox + '\'' +
                ", fieldNumber=" + fieldNumber +
                ", fieldText='" + fieldText + '\'' +
                ", fieldProvince='" + fieldProvince + '\'' +
                ", fieldCity='" + fieldCity + '\'' +
                ", fieldRegion='" + fieldRegion + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", imagePathList='" + imagePathList + '\'' +
                ", fieldTextarea='" + fieldTextarea + '\'' +
                ", fieldHtmleditor='" + fieldHtmleditor + '\'' +
                ", fieldHtmleditorCut='" + fieldHtmleditorCut + '\'' +
                '}';
    }
}
