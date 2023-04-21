package com.yesipov.gusto.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.yesipov.gusto.InformationActivity;
import com.yesipov.gusto.Orders.GetOrdersActivity;
import com.yesipov.gusto.MainActivity;
import com.yesipov.gusto.Models.Support;
import com.yesipov.gusto.Models.User;
import com.yesipov.gusto.R;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference refUsers = FirebaseDatabase.getInstance().getReference("users");
    DatabaseReference refMessages = FirebaseDatabase.getInstance().getReference("messages");
    TextView points, name, email, phone, birthday;
    Button btnExit, btnOrders, btnModify, btnSender, btnGetInfo;
    String pointsFromDB, nameFromDB;

    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        points = (TextView) getView().findViewById(R.id.points);
        name = (TextView) getView().findViewById(R.id.name);
        email = (TextView) getView().findViewById(R.id.email_info);
        phone = (TextView) getView().findViewById(R.id.phone_info);
        birthday = (TextView) getView().findViewById(R.id.birthday_info);
        btnExit = (Button) getView().findViewById(R.id.buttonExit);
        btnModify = (Button) getView().findViewById(R.id.buttonModifyData);
        btnOrders = (Button) getView().findViewById(R.id.buttonGetOrders);
        btnSender = (Button) getView().findViewById(R.id.buttonSendMessage);
        btnGetInfo = (Button) getView().findViewById(R.id.buttonGetAddress);
        btnExit.setOnClickListener(this);
        btnModify.setOnClickListener(this);
        btnOrders.setOnClickListener(this);
        btnSender.setOnClickListener(this);
        btnGetInfo.setOnClickListener(this);

        refUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);

                        points.setText(user.getPoints().toString() + " Б");
                        name.setText(user.getName() + " " + user.getSurname());
                        birthday.setText(user.getBirthday());
                        phone.setText(user.getPhone());
                        email.setText(user.getEmail());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonExit: {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
                break;
            }
            case R.id.buttonSendMessage: {
                showMessageWindow();
                break;
            }
            case R.id.buttonModifyData: {
                showChangeDataWindow();
                break;
            }
            case R.id.buttonGetOrders: {
                startActivity(new Intent(getContext(), GetOrdersActivity.class));
                break;
            }
            case R.id.buttonGetAddress: {
                startActivity(new Intent(getContext(), InformationActivity.class));
                break;
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    private void showChangeDataWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Изменение данных");

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View registerWindow = inflater.inflate(R.layout.change_data_activity, null);
        dialog.setView(registerWindow);

        final MaterialEditText emailCh = registerWindow.findViewById(R.id.emaiFieldChange);
        final MaterialEditText nameCh = registerWindow.findViewById(R.id.nameFieldChange);
        final MaterialEditText surnameCh = registerWindow.findViewById(R.id.surnameFieldChange);
        final MaterialEditText passwordCh = registerWindow.findViewById(R.id.passwordFieldChange);
        final MaterialEditText phoneCh = registerWindow.findViewById(R.id.phoneFieldChange);
        final MaterialEditText birthdayCh = registerWindow.findViewById(R.id.birthdayFieldChange);
        User userNew = new User();

        refUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);

                        surnameCh.setText(user.getSurname());
                        birthdayCh.setText(user.getBirthday());
                        nameCh.setText(user.getName());
                        phoneCh.setText(user.getPhone());
                        passwordCh.setText(user.getPass());
                        emailCh.setText(user.getEmail());
                        userNew.setPoints(user.getPoints());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        dialog.setPositiveButton("Изменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                userNew.setEmail(emailCh.getText().toString());
                userNew.setPhone(phoneCh.getText().toString());
                userNew.setPass(passwordCh.getText().toString());
                userNew.setName(nameCh.getText().toString());
                userNew.setSurname(surnameCh.getText().toString());
                userNew.setBirthday(birthdayCh.getText().toString());

                refUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(userNew)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Данные были изменены!", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        dialog.show();
    }

    private void showMessageWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Письмо в тех. поддержку");
        dialog.setMessage("Напишите нам свои претензии или пожелания");

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View signWindow = inflater.inflate(R.layout.sender_message, null);
        dialog.setView(signWindow);

        final EditText message = signWindow.findViewById(R.id.mesField);
        final MaterialEditText header = signWindow.findViewById(R.id.headField);

        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        dialog.setPositiveButton("Отправить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                Support mes = new Support();
                mes.setMessage(message.getText().toString());
                mes.setHeader(header.getText().toString());
                mes.setUserID(user.getUid());
                refUsers.child(user.getUid()).child("messages").child(Timestamp.now().toDate().toString()).setValue(mes).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Письмо было отправлено!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        dialog.show();
    }
}