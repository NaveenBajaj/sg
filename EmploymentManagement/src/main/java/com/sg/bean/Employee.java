package com.sg.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Employee {
    public String employeeId;
    public String firstName;
    public String lastName;
    public String aadharId;
    public String mobileNo;
    public String pf;
    public String esic;
    public String fatherName;
    public String position;
    public String address;
    public String salaryBasis;
    public String getSalaryBasis() {
		return salaryBasis;
	}

	public void setSalaryBasis(String salaryBasis) {
		this.salaryBasis = salaryBasis;
	}

	public boolean isPf() {
		return isPf;
	}

	public void setPf(boolean isPf) {
		this.isPf = isPf;
	}

	public boolean isEsic() {
		return isEsic;
	}

	public void setEsic(boolean isEsic) {
		this.isEsic = isEsic;
	}

	public boolean isPf;
    public boolean isEsic;


    public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAadharId() {
        return aadharId;
    }

    public void setAadharId(String aadharId) {
        this.aadharId = aadharId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPf() {
        return pf;
    }

    public void setPf(String pf) {
        this.pf = pf;
    }

    public String getEsic() {
        return esic;
    }

    public void setEsic(String esic) {
        this.esic = esic;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }
}
