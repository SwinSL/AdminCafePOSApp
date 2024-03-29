package com.example.admincafeposapp.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admincafeposapp.Adapters.TransactionsRecyclerViewAdapter;
import com.example.admincafeposapp.Model.Order;
import com.example.admincafeposapp.Model.Transaction;
import com.example.admincafeposapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class OrdersFragment extends Fragment {

    private static final int STORAGE_CODE = 1000;
    private Button btn, deleteConfirmTransactionBtn, deleteTransactionBtn;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference orderCollectionRef = db.collection("Orders");
    private ArrayList<Order> orderArrayList = new ArrayList<>();
    private ArrayList<Order> reportOrderArrayList = new ArrayList<>();
    private List<String> orderIDArrayList = new ArrayList<>();
    private List<String> salesArrayList = new ArrayList<>();
    private List<String> orderStatusArray = new ArrayList<>();
    private List<Boolean> memberArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TransactionsRecyclerViewAdapter transactionsRecyclerViewAdapter;
    private EditText editDeleteTransactionId;
    private String chosenDate;
    private TextView date_chosen;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI();
        buildRecycler();
        getOrders();
    }

    private void initUI() {
        deleteTransactionBtn = getActivity().findViewById(R.id.transactiondeleteButton);
        deleteTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupRemoveTransaction();
            }
        });

        Calendar c = Calendar.getInstance();
        date_chosen = getActivity().findViewById(R.id.tv_date);
        date_chosen.setText(DateFormat.getDateInstance(DateFormat.LONG,Locale.UK).format(c.getTime()));
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
        chosenDate = formatter.format(c.getTime());
        date_chosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datepicker();
            }
        });

        btn = getActivity().findViewById(R.id.print_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                    if(ActivityCompat.checkSelfPermission(getContext(),android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions,STORAGE_CODE);
                    }
                    else{
                        savePDF();
                    }
                }else{
                    savePDF();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case STORAGE_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    savePDF();
                }else{
                    Toast.makeText(getContext(),"Permission denied", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void buildRecycler() {
        recyclerView = getActivity().findViewById(R.id.transactions_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        transactionsRecyclerViewAdapter = new TransactionsRecyclerViewAdapter(this.getContext(), orderArrayList);
        recyclerView.setAdapter(transactionsRecyclerViewAdapter);
    }

    private void datepicker() {
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
                chosenDate = formatter.format(myCalendar.getTime());
                date_chosen.setText(DateFormat.getDateInstance(DateFormat.LONG,Locale.UK).format(myCalendar.getTime()));
            }

        };

        DatePickerDialog datepickerDialog = new DatePickerDialog(Objects.requireNonNull(getContext()), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        datepickerDialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());
        datepickerDialog.show();
    }

    private void getOrders(){
        orderArrayList.clear();
        if(orderCollectionRef != null) {
            orderCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            Order order = document.toObject(Order.class);
                            orderArrayList.add(order);
                            transactionsRecyclerViewAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }

    }

    private void PopupRemoveTransaction(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.remove_transactions_popup, null);
        final PopupWindow popupWindow = new PopupWindow(view, 400, WindowManager.LayoutParams.WRAP_CONTENT);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        editDeleteTransactionId = view.findViewById(R.id.deleteTransactionsIdFill);
        deleteConfirmTransactionBtn = view.findViewById(R.id.button_confirmDeleteTransactions);

        deleteConfirmTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Remove Transaction")
                        .setMessage("Are you sure you want remove this transaction?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(!editDeleteTransactionId.getText().toString().isEmpty())
                                {
                                    final String transactionRemove = editDeleteTransactionId.getText().toString();

                                    orderCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful())
                                            {
                                                for(DocumentSnapshot document: task.getResult())
                                                {
                                                    Order order = document.toObject(Order.class);
                                                    if(order.getOrder_id().equals(transactionRemove))
                                                    {
                                                        orderCollectionRef.document(document.getId()).delete();
                                                        Toast.makeText(getContext(),transactionRemove + " deleted", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(getContext(),"PLEASE ENTER A EXISTED TRANSACTION ID", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                getOrders();
                                            }
                                        }
                                    });

                                    popupWindow.dismiss();
                                }
                                else
                                {
                                    Toast.makeText(getContext(),"PLEASE ENTER A TRANSACTION ID", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                builder.show();

            }
        });
    }

    private void savePDF() {
        for(int i = 0; i < orderArrayList.size(); i++){
            if(orderArrayList.get(i).getOrder_date().equals(chosenDate) && !(orderArrayList.get(i).getOrder_status().equals("Cancelled"))){
                reportOrderArrayList.add(orderArrayList.get(i));
            }
        }

        if(!reportOrderArrayList.isEmpty()){
            for(int i = 0; i < reportOrderArrayList.size(); i++){
                    orderIDArrayList.add(reportOrderArrayList.get(i).getOrder_id());
                    if(reportOrderArrayList.get(i).getIsMember().equals(true)){
                        memberArrayList.add(true);
                        salesArrayList.add(String.format("%.2f",reportOrderArrayList.get(i).getOrder_total() * 0.9));
                    }
                    else{
                        memberArrayList.add(false);
                        salesArrayList.add(String.format("%.2f",reportOrderArrayList.get(i).getOrder_total()));
                    }
                    orderStatusArray.add(reportOrderArrayList.get(i).getOrder_status());
            }

            Transaction myTransaction = new Transaction(chosenDate, orderIDArrayList, salesArrayList, memberArrayList,orderStatusArray);
            com.itextpdf.text.Document mDoc = new Document(PageSize.A4);
            String filename = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
            String filepath = Environment.getExternalStorageDirectory() + "/" + filename + ".pdf";

            System.out.println(myTransaction.getOrderID());

            try{
                PdfWriter.getInstance(mDoc, new FileOutputStream(filepath));

                PdfPTable table = new PdfPTable(new float[]{3,3,3,3});
                table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                table.getDefaultCell().setFixedHeight(50);
                table.setTotalWidth(PageSize.A4.getWidth());
                table.setWidthPercentage(100);
                table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell("Order ID");
                table.addCell("Member");
                table.addCell("Order Total");
                table.addCell("Order Status");
                table.setHeaderRows(1);
                PdfPCell[] cells = table.getRow(0).getCells();
                for(int i = 0; i < cells.length; i++){
                    cells[i].setBackgroundColor(BaseColor.GRAY);
                }
                Double total = 0.00;
                for(int i = 0; i < reportOrderArrayList.size(); i++){
                    table.addCell(myTransaction.getOrderID().get(i));
                    table.addCell(myTransaction.getMember().get(i).toString());
                    table.addCell(myTransaction.getSales().get(i));
                    table.addCell(myTransaction.getOrderStatus().get(i));
                    total += Double.parseDouble(myTransaction.getSales().get(i));
                }

                mDoc.open();

                Font title = new Font(Font.FontFamily.TIMES_ROMAN, 30.0f, Font.BOLD, BaseColor.BLACK);
                Font title1 = new Font(Font.FontFamily.TIMES_ROMAN, 24.0f, Font.NORMAL, BaseColor.BLACK);

                mDoc.add(new Paragraph("Cafe Daily Report\n", title));
                mDoc.add(new Paragraph("Transaction\n", title1));
                mDoc.add(new Paragraph("Date: " + date_chosen.getText() + "\n\n"));
                mDoc.add(table);
                mDoc.add(new Paragraph("\n\nTotal Sales: " + String.format("%.2f",total), title1));

                mDoc.close();
                myTransaction.reset();
                Toast.makeText(getContext(), filename + ".pdf\nis saved to\n " + filepath, Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(getContext(),"Transaction is empty for the day!", Toast.LENGTH_SHORT).show();
        }
        reportOrderArrayList.clear();
    }
}
