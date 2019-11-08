package com.example.admincafeposapp.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrdersFragment extends Fragment {

    private static final int STORAGE_CODE = 1000;
    private Button btn;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference orderCollectionRef = db.collection("Orders");
    private ArrayList<Order> myOrder = new ArrayList<>();
    private List<String> orderID = new ArrayList<>();
    private List<String> sales = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn = view.findViewById(R.id.print_btn);

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

    private void savePDF() {
        orderCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot documentSnapshot : task.getResult()){
                        Order order = documentSnapshot.toObject(Order.class);
                        for (Order orderItem: myOrder){
                            if (order.getOrder_id().equals(orderItem.getOrder_id())){
                                orderItem.setOrderItemArrayList(order.getOrderItemArrayList());
                            }
                        }
                        myOrder.add(order);
                    }

                    for(int i = 0; i < myOrder.size(); i++){
                        if(myOrder.get(i).getOrder_date().equals("02112019")){
                            orderID.add(myOrder.get(i).getOrder_id());
                            sales.add(String.valueOf(myOrder.get(i).getOrder_total()));
                        }
                    }

                    Transaction myTransaction = new Transaction("02112019", orderID, sales);

                    com.itextpdf.text.Document mDoc = new Document(PageSize.A4);
                    String filename = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
                    String filepath = Environment.getExternalStorageDirectory() + "/" + filename + ".pdf";

                    try{
                        PdfWriter.getInstance(mDoc, new FileOutputStream(filepath));

                        PdfPTable table = new PdfPTable(new float[]{3,3});
                        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.getDefaultCell().setFixedHeight(50);
                        table.setTotalWidth(PageSize.A4.getWidth());
                        table.setWidthPercentage(100);
                        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
                        table.addCell("Order ID");
                        table.addCell("Order_Total");
                        table.setHeaderRows(1);
                        PdfPCell[] cells = table.getRow(0).getCells();
                        for(int i = 0; i < cells.length; i++){
                            cells[i].setBackgroundColor(BaseColor.GRAY);
                        }
                        Double total = 0.00;
                        for(int i = 0; i < orderID.size(); i++){
                            table.addCell(myTransaction.getOrderID().get(i));
                            table.addCell(myTransaction.getSales().get(i));
                            total += Double.parseDouble(myTransaction.getSales().get(i));
                        }

                        mDoc.open();

                        Font title = new Font(Font.FontFamily.TIMES_ROMAN, 30.0f, Font.BOLD, BaseColor.BLACK);
                        Font title1 = new Font(Font.FontFamily.TIMES_ROMAN, 24.0f, Font.NORMAL, BaseColor.BLACK);

                        mDoc.add(new Paragraph("Cafe Daily Report\n", title));
                        mDoc.add(new Paragraph("Transaction\n", title1));
                        mDoc.add(new Paragraph("Date: " + myTransaction.getDate() + "\n\n"));
                        mDoc.add(table);
                        mDoc.add(new Paragraph("\n\nTotal Sales: " + total, title1));

                        mDoc.close();


                        Toast.makeText(getContext(), filename + ".pdf\nis saved to\n " + filepath, Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
