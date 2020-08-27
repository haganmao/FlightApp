package com.example.flightapp.Model;

import java.util.List;

public class Request {

    private String phone;
    private String address;
    private String name;
    private String total;
    private String status;
    private List<Order> tickets; //list of tickets

    public Request() {
    }

    public Request(String phone, String address, String name, String total, List<Order> tickets) {
        this.phone = phone;
        this.address = address;
        this.name = name;
        this.total = total;
        this.tickets = tickets;
        this.status = "0"; //Set 0 as default means request placed, 1 means pending, 2 means confirmed
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getTickets() {
        return tickets;
    }

    public void setTickets(List<Order> tickets) {
        this.tickets = tickets;
    }
}
