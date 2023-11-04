package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {

    private Context context;
    private List<Contact> contactList;

    public ContactsAdapter(Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_item, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);

        String name = contact.getName();
        String dob = contact.getDob();
        String email = contact.getEmail();
        String profileImageResource = contact.getProfileImageResource();

        holder.contactNameTextView.setText(name);
        holder.contactDOBTextView.setText(dob);
        holder.contactEmailTextView.setText(email);
        holder.contactImageView.setImageResource(context.getResources().getIdentifier(profileImageResource, "drawable", context.getPackageName()));
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {
        ImageView contactImageView;
        TextView contactNameTextView;
        TextView contactDOBTextView;
        TextView contactEmailTextView;

        ContactViewHolder(View itemView) {
            super(itemView);
            contactImageView = itemView.findViewById(R.id.contactImageView);
            contactNameTextView = itemView.findViewById(R.id.contactNameTextView);
            contactDOBTextView = itemView.findViewById(R.id.contactDOBTextView);
            contactEmailTextView = itemView.findViewById(R.id.contactEmailTextView);
        }
    }
}
