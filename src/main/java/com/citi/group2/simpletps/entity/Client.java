package com.citi.group2.simpletps.entity;

public class Client {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column client.c_id
     *
     * @mbggenerated
     */
    private Integer cId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column client.c_name
     *
     * @mbggenerated
     */
    private String cName;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column client.c_id
     *
     * @return the value of client.c_id
     *
     * @mbggenerated
     */
    public Integer getcId() {
        return cId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column client.c_id
     *
     * @param cId the value for client.c_id
     *
     * @mbggenerated
     */
    public void setcId(Integer cId) {
        this.cId = cId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column client.c_name
     *
     * @return the value of client.c_name
     *
     * @mbggenerated
     */
    public String getcName() {
        return cName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column client.c_name
     *
     * @param cName the value for client.c_name
     *
     * @mbggenerated
     */
    public void setcName(String cName) {
        this.cName = cName == null ? null : cName.trim();
    }
}