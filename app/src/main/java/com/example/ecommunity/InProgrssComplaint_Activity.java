package com.example.ecommunity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class InProgrssComplaint_Activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference myref;
    LinearLayoutManager linearLayoutManager;
    FirebaseUser firebaseUser;
    FirebaseAuth myAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_progrss_complaint_);

        myAuth = FirebaseAuth.getInstance();
        firebaseUser = myAuth.getCurrentUser();

        myref= FirebaseDatabase.getInstance().getReference("App").child("UserFeedbacks").child(firebaseUser.getUid()).child("Complaint Details");
        myref.keepSynced(true);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        FirebaseRecyclerAdapter<FeedBack_DataModel,BlogViewHolder> recyclerAdapter=
                new FirebaseRecyclerAdapter<FeedBack_DataModel, BlogViewHolder>(
                        FeedBack_DataModel.class,
                        R.layout.inprogress_cardlayout,
                        BlogViewHolder.class,
                        myref
                ) {
                    @Override
                    protected void populateViewHolder(BlogViewHolder viewHolder, FeedBack_DataModel model, int position) {
                        viewHolder.setComplaintDept(model.getComplaintDept());
                        viewHolder.setComplaintType(model.getComplaintType());
                        viewHolder.setComplaintCategory(model.getComplaintCategory());
                        viewHolder.setComplaintDetails(model.getComplaintDetails());
                        viewHolder.setComplaintLocation(model.getComplaintLocation());
                        viewHolder.setPostalAddress(model.getPostalAddress());
                        viewHolder.setImage(model.getImage());
                        //progressDialog.dismiss();
                    }
                };
        recyclerView.setAdapter(recyclerAdapter);

    }
    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView tv_ComplaintDept,tv_ComplaintType,tv_ComplaintCategory,tv_ComplaintDetails,tv_ComplaintLocation,tv_ComplaintPostalAddress;
        ImageView imageView_attachments;
        public BlogViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            tv_ComplaintDept = (TextView)itemView.findViewById(R.id.txt_ComplaintDept);
            tv_ComplaintType = (TextView) itemView.findViewById(R.id.txt_ComplaintType);
            tv_ComplaintCategory = (TextView) itemView.findViewById(R.id.txt_ComplaintCategory);
            tv_ComplaintDetails = (TextView) itemView.findViewById(R.id.txt_ComplaintDetails);
            tv_ComplaintLocation = (TextView) itemView.findViewById(R.id.txt_ComplaintLocation);
            tv_ComplaintPostalAddress = (TextView) itemView.findViewById(R.id.txt_ComplaintPostalAddress);

            imageView_attachments = (ImageView) itemView.findViewById(R.id.imageview_ComplaintAttachments);

        }
        public void setComplaintDept(String complaintDept)
        {
            tv_ComplaintDept.setText(complaintDept);
        }
        public void setComplaintType(String complaintType)
        {
            tv_ComplaintType.setText(complaintType);
        }

        public void setComplaintCategory(String complaintCategory)
        {
            tv_ComplaintType.setText(complaintCategory);
        }
        public void setComplaintDetails(String complaintDetails)
        {
            tv_ComplaintType.setText(complaintDetails);
        }
        public void setComplaintLocation(String complaintLocation)
        {
            tv_ComplaintType.setText(complaintLocation);
        }
        public void setPostalAddress(String postalAddress)
        {
            tv_ComplaintType.setText(postalAddress);
        }
        public void setImage(String image)
        {
            Picasso.get().load(image).into(imageView_attachments);
        }

    }
}
