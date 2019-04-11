package com.example.ecommunity;

public class FeedBack_DataModel {
     private String UserName,Feedback;
     private  String ComplaintDept,ComplaintType,ComplaintCategory,ComplaintDetails,ComplaintLocation,PostalAddress,Image;

    public FeedBack_DataModel() {
    }

    public FeedBack_DataModel(String userName, String feedback) {
        UserName = userName;
        Feedback = feedback;
    }

    public FeedBack_DataModel(String complaintDept, String complaintType, String complaintCategory, String complaintDetails, String complaintLocation, String postalAddress, String image) {
        ComplaintDept = complaintDept;
        ComplaintType = complaintType;
        ComplaintCategory = complaintCategory;
        ComplaintDetails = complaintDetails;
        ComplaintLocation = complaintLocation;
        PostalAddress = postalAddress;
        Image = image;
    }


    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getFeedback() {
        return Feedback;
    }

    public void setFeedback(String feedback) {
        Feedback = feedback;
    }




    public String getComplaintDept() {
        return ComplaintDept;
    }

    public void setComplaintDept(String complaintDept) {
        ComplaintDept = complaintDept;
    }

    public String getComplaintType() {
        return ComplaintType;
    }

    public void setComplaintType(String complaintType) {
        ComplaintType = complaintType;
    }

    public String getComplaintCategory() {
        return ComplaintCategory;
    }

    public void setComplaintCategory(String complaintCategory) {
        ComplaintCategory = complaintCategory;
    }

    public String getComplaintDetails() {
        return ComplaintDetails;
    }

    public void setComplaintDetails(String complaintDetails) {
        ComplaintDetails = complaintDetails;
    }

    public String getComplaintLocation() {
        return ComplaintLocation;
    }

    public void setComplaintLocation(String complaintLocation) {
        ComplaintLocation = complaintLocation;
    }

    public String getPostalAddress() {
        return PostalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        PostalAddress = postalAddress;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
