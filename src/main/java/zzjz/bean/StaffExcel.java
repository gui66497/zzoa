package zzjz.bean;

import org.jeecgframework.poi.excel.annotation.Excel;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * @ClassName: StaffExcel
 * @Description: 员工表(Excel用)
 * @author 房桂堂
 * @date 2017年3月10日 下午1:32:29
 */
@XmlRootElement
public class StaffExcel {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.staff_id
     *
     * @mbggenerated
     */
    private long staffId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.name
     *
     * @mbggenerated
     */
        @Excel(name = "姓名")
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.department
     *
     * @mbggenerated
     */
    @Excel(name = "部门")
    private String department;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.position
     *
     * @mbggenerated
     */
    @Excel(name = "职位")
    private String position;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.entry_date
     *
     * @mbggenerated
     */
    @Excel(name = "入职日期", format = "yyyy/MM/dd")
    private String entryDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.is_formal
     *
     * @mbggenerated
     */
    @Excel(name = "是否转正" ,replace = {"是_1", "否_0"})
    private Integer isFormal;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.formal_date
     *
     * @mbggenerated
     */
    @Excel(name = "转正日期", format = "yyyy/MM/dd")
    private String formalDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.company_age
     *
     * @mbggenerated
     */
    @Excel(name = "司龄")
    private Float companyAge;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.id_number
     *
     * @mbggenerated
     */
    @Excel(name = "身份证号")
    private String idNumber;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.birth
     *
     * @mbggenerated
     */
    //通过身份证号获取
    @Excel(name = "出生日期", format = "yyyy/MM/dd")
    private String birth;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.age
     *
     * @mbggenerated
     */
    @Excel(name = "年龄")
    private Integer age;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.gender
     *
     * @mbggenerated
     */
    @Excel(name = "性别", replace = {"男_1", "女_0"})
    private Integer gender;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.pre_seniority
     *
     * @mbggenerated
     */
    @Excel(name = "入职前工龄")
    private Float preSeniority;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.seniority
     *
     * @mbggenerated
     */
    @Excel(name = "工龄")
    private String seniority;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.pay_card
     *
     * @mbggenerated
     */
    @Excel(name = "工资卡号")
    private String payCard;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.political_status
     *
     * @mbggenerated
     */
    @Excel(name = "政治面貌")
    private String politicalStatus;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.job_number
     *
     * @mbggenerated
     */
    @Excel(name = "工号")
    private String jobNumber;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.contract_start
     *
     * @mbggenerated
     */
    @Excel(name = "合同起始日期", format = "yyyy/MM/dd")
    private String contractStart;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.contract_end
     *
     * @mbggenerated
     */
    @Excel(name = "合同结束日期", format = "yyyy/MM/dd")
    private String contractEnd;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.tel
     *
     * @mbggenerated
     */
    @Excel(name = "电话")
    private String tel;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.graduate_school
     *
     * @mbggenerated
     */
    @Excel(name = "毕业院校")
    private String graduateSchool;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.major
     *
     * @mbggenerated
     */
    @Excel(name = "专业")
    private String major;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.education
     *
     * @mbggenerated
     */
    @Excel(name = "学历")
    private String education;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.graduate_date
     *
     * @mbggenerated
     */
    @Excel(name = "毕业日期", format = "yyyy/MM/dd")
    private String graduateDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.professional_level
     *
     * @mbggenerated
     */
    @Excel(name = "职称/资格级别")
    private String professionalLevel;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.professional_name
     *
     * @mbggenerated
     */
    @Excel(name = "职称/资格名称")
    private String professionalName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.certificate_validity
     *
     * @mbggenerated
     */
    @Excel(name = "证书有效期", format = "yyyy/MM/dd")
    private String certificateValidity;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.native_place
     *
     * @mbggenerated
     */
    @Excel(name = "籍贯")
    private String nativePlace;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.home_address
     *
     * @mbggenerated
     */
    @Excel(name = "家庭住址")
    private String homeAddress;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.census_register
     *
     * @mbggenerated
     */
    @Excel(name = "户籍")
    private String censusRegister;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.file_keep
     *
     * @mbggenerated
     */
    @Excel(name = "档案保管（变更）")
    private String fileKeep;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.emergency_contactor
     *
     * @mbggenerated
     */
    @Excel(name = "紧急联系人")
    private String emergencyContactor;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.emergency_tel
     *
     * @mbggenerated
     */
    @Excel(name = "联系电话")
    private String emergencyTel;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.company
     *
     * @mbggenerated
     */
    @Excel(name = "所属公司")
    private String company;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.on_job
     *
     * @mbggenerated
     */
    @Excel(name = "在职/离职", replace = {"在职_1", "离职_0"})
    private Integer onJob;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.leave_time
     *
     * @mbggenerated
     */
    @Excel(name = "离职日期", format = "yyyy/MM/dd")
    private String leaveTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.it_attributes
     *
     * @mbggenerated
     */
    @Excel(name = "高新技术\n" + "人员属性")
    private String itAttributes;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.research_staff
     *
     * @mbggenerated
     */
    @Excel(name = "高新技术\n" + "研发人员")
    private String researchStaff;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_staff.in_assurance
     *
     * @mbggenerated
     */
    @Excel(name = "是否参加社保", replace = {"是_1", "否_0"})
    private Integer inAssurance;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.id
     *
     * @return the value of tb_staff.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.id
     *
     * @param id the value for tb_staff.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.staff_id
     *
     * @return the value of tb_staff.staff_id
     *
     * @mbggenerated
     */
    public long getStaffId() {
        return staffId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.staff_id
     *
     * @param staffId the value for tb_staff.staff_id
     *
     * @mbggenerated
     */
    public void setStaffId(long staffId) {
        this.staffId = staffId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.name
     *
     * @return the value of tb_staff.name
     *
     * @mbggenerated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.name
     *
     * @param name the value for tb_staff.name
     *
     * @mbggenerated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.department
     *
     * @return the value of tb_staff.department
     *
     * @mbggenerated
     */
    public String getDepartment() {
        return department;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.department
     *
     * @param department the value for tb_staff.department
     *
     * @mbggenerated
     */
    public void setDepartment(String department) {
        this.department = department == null ? null : department.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.position
     *
     * @return the value of tb_staff.position
     *
     * @mbggenerated
     */
    public String getPosition() {
        return position;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.position
     *
     * @param position the value for tb_staff.position
     *
     * @mbggenerated
     */
    public void setPosition(String position) {
        this.position = position == null ? null : position.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.entry_date
     *
     * @return the value of tb_staff.entry_date
     *
     * @mbggenerated
     */
    public String getEntryDate() {
        return entryDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.entry_date
     *
     * @param entryDate the value for tb_staff.entry_date
     *
     * @mbggenerated
     */
    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.is_formal
     *
     * @return the value of tb_staff.is_formal
     *
     * @mbggenerated
     */
    public Integer getIsFormal() {
        return isFormal;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.is_formal
     *
     * @param isFormal the value for tb_staff.is_formal
     *
     * @mbggenerated
     */
    public void setIsFormal(Integer isFormal) {
        this.isFormal = isFormal;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.formal_date
     *
     * @return the value of tb_staff.formal_date
     *
     * @mbggenerated
     */
    public String getFormalDate() {
        return formalDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.formal_date
     *
     * @param formalDate the value for tb_staff.formal_date
     *
     * @mbggenerated
     */
    public void setFormalDate(String formalDate) {
        this.formalDate = formalDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.company_age
     *
     * @return the value of tb_staff.company_age
     *
     * @mbggenerated
     */
    public Float getCompanyAge() {
        return companyAge;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.company_age
     *
     * @param companyAge the value for tb_staff.company_age
     *
     * @mbggenerated
     */
    public void setCompanyAge(Float companyAge) {
        this.companyAge = companyAge;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.id_number
     *
     * @return the value of tb_staff.id_number
     *
     * @mbggenerated
     */
    public String getIdNumber() {
        return idNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.id_number
     *
     * @param idNumber the value for tb_staff.id_number
     *
     * @mbggenerated
     */
    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber == null ? null : idNumber.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.birth
     *
     * @return the value of tb_staff.birth
     *
     * @mbggenerated
     */
    public String getBirth() {
        return birth;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.birth
     *
     * @param birth the value for tb_staff.birth
     *
     * @mbggenerated
     */
    public void setBirth(String birth) {
        this.birth = birth;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.age
     *
     * @return the value of tb_staff.age
     *
     * @mbggenerated
     */
    public Integer getAge() {
        return age;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.age
     *
     * @param age the value for tb_staff.age
     *
     * @mbggenerated
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.gender
     *
     * @return the value of tb_staff.gender
     *
     * @mbggenerated
     */
    public Integer getGender() {
        return gender;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.gender
     *
     * @param gender the value for tb_staff.gender
     *
     * @mbggenerated
     */
    public void setGender(Integer gender) {
        this.gender = gender;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.pre_seniority
     *
     * @return the value of tb_staff.pre_seniority
     *
     * @mbggenerated
     */
    public Float getPreSeniority() {
        return preSeniority;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.pre_seniority
     *
     * @param preSeniority the value for tb_staff.pre_seniority
     *
     * @mbggenerated
     */
    public void setPreSeniority(Float preSeniority) {
        this.preSeniority = preSeniority;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.seniority
     *
     * @return the value of tb_staff.seniority
     *
     * @mbggenerated
     */
    public String getSeniority() {
        return seniority;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.seniority
     *
     * @param seniority the value for tb_staff.seniority
     *
     * @mbggenerated
     */
    public void setSeniority(String seniority) {
        this.seniority = seniority;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.pay_card
     *
     * @return the value of tb_staff.pay_card
     *
     * @mbggenerated
     */
    public String getPayCard() {
        return payCard;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.pay_card
     *
     * @param payCard the value for tb_staff.pay_card
     *
     * @mbggenerated
     */
    public void setPayCard(String payCard) {
        this.payCard = payCard == null ? null : payCard.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.political_status
     *
     * @return the value of tb_staff.political_status
     *
     * @mbggenerated
     */
    public String getPoliticalStatus() {
        return politicalStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.political_status
     *
     * @param politicalStatus the value for tb_staff.political_status
     *
     * @mbggenerated
     */
    public void setPoliticalStatus(String politicalStatus) {
        this.politicalStatus = politicalStatus == null ? null : politicalStatus.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.job_number
     *
     * @return the value of tb_staff.job_number
     *
     * @mbggenerated
     */
    public String getJobNumber() {
        return jobNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.job_number
     *
     * @param jobNumber the value for tb_staff.job_number
     *
     * @mbggenerated
     */
    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber == null ? null : jobNumber.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.contract_start
     *
     * @return the value of tb_staff.contract_start
     *
     * @mbggenerated
     */
    public String getContractStart() {
        return contractStart;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.contract_start
     *
     * @param contractStart the value for tb_staff.contract_start
     *
     * @mbggenerated
     */
    public void setContractStart(String contractStart) {
        this.contractStart = contractStart;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.contract_end
     *
     * @return the value of tb_staff.contract_end
     *
     * @mbggenerated
     */
    public String getContractEnd() {
        return contractEnd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.contract_end
     *
     * @param contractEnd the value for tb_staff.contract_end
     *
     * @mbggenerated
     */
    public void setContractEnd(String contractEnd) {
        this.contractEnd = contractEnd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.tel
     *
     * @return the value of tb_staff.tel
     *
     * @mbggenerated
     */
    public String getTel() {
        return tel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.tel
     *
     * @param tel the value for tb_staff.tel
     *
     * @mbggenerated
     */
    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.graduate_school
     *
     * @return the value of tb_staff.graduate_school
     *
     * @mbggenerated
     */
    public String getGraduateSchool() {
        return graduateSchool;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.graduate_school
     *
     * @param graduateSchool the value for tb_staff.graduate_school
     *
     * @mbggenerated
     */
    public void setGraduateSchool(String graduateSchool) {
        this.graduateSchool = graduateSchool == null ? null : graduateSchool.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.major
     *
     * @return the value of tb_staff.major
     *
     * @mbggenerated
     */
    public String getMajor() {
        return major;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.major
     *
     * @param major the value for tb_staff.major
     *
     * @mbggenerated
     */
    public void setMajor(String major) {
        this.major = major == null ? null : major.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.education
     *
     * @return the value of tb_staff.education
     *
     * @mbggenerated
     */
    public String getEducation() {
        return education;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.education
     *
     * @param education the value for tb_staff.education
     *
     * @mbggenerated
     */
    public void setEducation(String education) {
        this.education = education == null ? null : education.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.graduate_date
     *
     * @return the value of tb_staff.graduate_date
     *
     * @mbggenerated
     */
    public String getGraduateDate() {
        return graduateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.graduate_date
     *
     * @param graduateDate the value for tb_staff.graduate_date
     *
     * @mbggenerated
     */
    public void setGraduateDate(String graduateDate) {
        this.graduateDate = graduateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.professional_level
     *
     * @return the value of tb_staff.professional_level
     *
     * @mbggenerated
     */
    public String getProfessionalLevel() {
        return professionalLevel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.professional_level
     *
     * @param professionalLevel the value for tb_staff.professional_level
     *
     * @mbggenerated
     */
    public void setProfessionalLevel(String professionalLevel) {
        this.professionalLevel = professionalLevel == null ? null : professionalLevel.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.professional_name
     *
     * @return the value of tb_staff.professional_name
     *
     * @mbggenerated
     */
    public String getProfessionalName() {
        return professionalName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.professional_name
     *
     * @param professionalName the value for tb_staff.professional_name
     *
     * @mbggenerated
     */
    public void setProfessionalName(String professionalName) {
        this.professionalName = professionalName == null ? null : professionalName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.certificate_validity
     *
     * @return the value of tb_staff.certificate_validity
     *
     * @mbggenerated
     */
    public String getCertificateValidity() {
        return certificateValidity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.certificate_validity
     *
     * @param certificateValidity the value for tb_staff.certificate_validity
     *
     * @mbggenerated
     */
    public void setCertificateValidity(String certificateValidity) {
        this.certificateValidity = certificateValidity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.native_place
     *
     * @return the value of tb_staff.native_place
     *
     * @mbggenerated
     */
    public String getNativePlace() {
        return nativePlace;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.native_place
     *
     * @param nativePlace the value for tb_staff.native_place
     *
     * @mbggenerated
     */
    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace == null ? null : nativePlace.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.home_address
     *
     * @return the value of tb_staff.home_address
     *
     * @mbggenerated
     */
    public String getHomeAddress() {
        return homeAddress;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.home_address
     *
     * @param homeAddress the value for tb_staff.home_address
     *
     * @mbggenerated
     */
    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress == null ? null : homeAddress.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.census_register
     *
     * @return the value of tb_staff.census_register
     *
     * @mbggenerated
     */
    public String getCensusRegister() {
        return censusRegister;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.census_register
     *
     * @param censusRegister the value for tb_staff.census_register
     *
     * @mbggenerated
     */
    public void setCensusRegister(String censusRegister) {
        this.censusRegister = censusRegister == null ? null : censusRegister.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.file_keep
     *
     * @return the value of tb_staff.file_keep
     *
     * @mbggenerated
     */
    public String getFileKeep() {
        return fileKeep;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.file_keep
     *
     * @param fileKeep the value for tb_staff.file_keep
     *
     * @mbggenerated
     */
    public void setFileKeep(String fileKeep) {
        this.fileKeep = fileKeep == null ? null : fileKeep.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.emergency_contactor
     *
     * @return the value of tb_staff.emergency_contactor
     *
     * @mbggenerated
     */
    public String getEmergencyContactor() {
        return emergencyContactor;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.emergency_contactor
     *
     * @param emergencyContactor the value for tb_staff.emergency_contactor
     *
     * @mbggenerated
     */
    public void setEmergencyContactor(String emergencyContactor) {
        this.emergencyContactor = emergencyContactor == null ? null : emergencyContactor.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.emergency_tel
     *
     * @return the value of tb_staff.emergency_tel
     *
     * @mbggenerated
     */
    public String getEmergencyTel() {
        return emergencyTel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.emergency_tel
     *
     * @param emergencyTel the value for tb_staff.emergency_tel
     *
     * @mbggenerated
     */
    public void setEmergencyTel(String emergencyTel) {
        this.emergencyTel = emergencyTel == null ? null : emergencyTel.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.company
     *
     * @return the value of tb_staff.company
     *
     * @mbggenerated
     */
    public String getCompany() {
        return company;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.company
     *
     * @param company the value for tb_staff.company
     *
     * @mbggenerated
     */
    public void setCompany(String company) {
        this.company = company == null ? null : company.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.on_job
     *
     * @return the value of tb_staff.on_job
     *
     * @mbggenerated
     */
    public Integer getOnJob() {
        return onJob;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.on_job
     *
     * @param onJob the value for tb_staff.on_job
     *
     * @mbggenerated
     */
    public void setOnJob(Integer onJob) {
        this.onJob = onJob;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.leave_time
     *
     * @return the value of tb_staff.leave_time
     *
     * @mbggenerated
     */
    public String getLeaveTime() {
        return leaveTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.leave_time
     *
     * @param leaveTime the value for tb_staff.leave_time
     *
     * @mbggenerated
     */
    public void setLeaveTime(String leaveTime) {
        this.leaveTime = leaveTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.it_attributes
     *
     * @return the value of tb_staff.it_attributes
     *
     * @mbggenerated
     */
    public String getItAttributes() {
        return itAttributes;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.it_attributes
     *
     * @param itAttributes the value for tb_staff.it_attributes
     *
     * @mbggenerated
     */
    public void setItAttributes(String itAttributes) {
        this.itAttributes = itAttributes == null ? null : itAttributes.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.research_staff
     *
     * @return the value of tb_staff.research_staff
     *
     * @mbggenerated
     */
    public String getResearchStaff() {
        return researchStaff;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.research_staff
     *
     * @param researchStaff the value for tb_staff.research_staff
     *
     * @mbggenerated
     */
    public void setResearchStaff(String researchStaff) {
        this.researchStaff = researchStaff == null ? null : researchStaff.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_staff.in_assurance
     *
     * @return the value of tb_staff.in_assurance
     *
     * @mbggenerated
     */
    public Integer getInAssurance() {
        return inAssurance;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_staff.in_assurance
     *
     * @param inAssurance the value for tb_staff.in_assurance
     *
     * @mbggenerated
     */
    public void setInAssurance(Integer inAssurance) {
        this.inAssurance = inAssurance;
    }
}