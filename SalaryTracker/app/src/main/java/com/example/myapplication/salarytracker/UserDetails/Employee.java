package com.example.myapplication.salarytracker.UserDetails;

import android.os.Parcel;
import android.os.Parcelable;

public class Employee implements Parcelable {

    private String name, emailid, phoneno, post, edu_qual;
    private String base_salary, num_leaves;

    public Employee() {}

    public Employee(String name, String emailid, String phoneno, String post, String edu_qual, String base_salary, String leaves) {
        this.name = name;
        this.emailid = emailid;
        this.phoneno = phoneno;
        this.post = post;
        this.edu_qual = edu_qual;
        this.base_salary = base_salary;
        this.num_leaves = leaves;
    }

    protected Employee(Parcel in) {
        name = in.readString();
        emailid = in.readString();
        phoneno = in.readString();
        post = in.readString();
        edu_qual = in.readString();
        base_salary = in.readString();
        num_leaves = in.readString();
    }

    public static final Creator<Employee> CREATOR = new Creator<Employee>() {
        @Override
        public Employee createFromParcel(Parcel in) {
            return new Employee(in);
        }

        @Override
        public Employee[] newArray(int size) {
            return new Employee[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getEmailid() {
        return emailid;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public String getPost() {
        return post;
    }

    public String getEdu_qual() {
        return edu_qual;
    }

    public String getBase_salary() {
        return base_salary;
    }

    public String getNum_leaves() {
        return num_leaves;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(emailid);
        dest.writeString(phoneno);
        dest.writeString(post);
        dest.writeString(edu_qual);
        dest.writeString(base_salary);
        dest.writeString(num_leaves);
    }
}
