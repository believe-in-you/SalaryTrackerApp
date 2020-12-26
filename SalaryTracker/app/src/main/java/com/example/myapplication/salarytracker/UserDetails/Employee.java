package com.example.myapplication.salarytracker.UserDetails;

import android.os.Parcel;
import android.os.Parcelable;

public class Employee implements Parcelable {

    private String name, emailid, phoneno, post, edu_qual;
    private String base_salary;
    private String num_leaves;
    private String unpaid_leaves_amount;
    private int due_date, due_month, due_year;

    public Employee() {}

    public Employee(String name, String emailid, String phoneno, String post, String edu_qual, String base_salary,
                    String leaves, int due_date, int due_month, int due_year, String unpaid_leaves_amount) {
        this.name = name;
        this.emailid = emailid;
        this.phoneno = phoneno;
        this.post = post;
        this.edu_qual = edu_qual;
        this.base_salary = base_salary;
        this.num_leaves = leaves;
        this.due_date = due_date;
        this.due_month = due_month;
        this.due_year = due_year;
        this.unpaid_leaves_amount = unpaid_leaves_amount;
    }

    protected Employee(Parcel in) {
        name = in.readString();
        emailid = in.readString();
        phoneno = in.readString();
        post = in.readString();
        edu_qual = in.readString();
        base_salary = in.readString();
        num_leaves = in.readString();
        unpaid_leaves_amount = in.readString();
        due_date = in.readInt();
        due_month = in.readInt();
        due_year = in.readInt();
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

    public int getDue_date() {
        return due_date;
    }

    public int getDue_month() {
        return due_month;
    }

    public int getDue_year() {
        return due_year;
    }

    public void setDue_date(int date) { this.due_date = date; }

    public void setDue_month(int month) { this.due_month = month; }

    public void setDue_year(int year) { this.due_year = year; }

    public void setNum_leaves(String leaves) { this.num_leaves = leaves; }

    public String getUnpaid_leaves_amount() {
        return unpaid_leaves_amount;
    }

    public void setUnpaid_leaves_amount(String unpaid_leaves_amount) {
        this.unpaid_leaves_amount = unpaid_leaves_amount;
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
        dest.writeString(unpaid_leaves_amount);
        dest.writeInt(due_date);
        dest.writeInt(due_month);
        dest.writeInt(due_year);
    }
}
