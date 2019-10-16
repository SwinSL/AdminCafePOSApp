package com.example.admincafeposapp.Model;

public class Tables {

    String tableNo, tableStatus;
    int tableNoOfSeat;

    public Tables(){}

    public Tables(String tableNo,int tableNoOfSeat, String tableStatus) {
        this.tableNo = tableNo;
        this.tableStatus = tableStatus;
        this.tableNoOfSeat = tableNoOfSeat;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public String getTableStatus() {
        return tableStatus;
    }

    public void setTableStatus(String tableStatus) {
        this.tableStatus = tableStatus;
    }

    public int getTableNoOfSeat() {
        return tableNoOfSeat;
    }

    public void setTableNoOfSeat(int tableNoOfSeat) {
        this.tableNoOfSeat = tableNoOfSeat;
    }
}
