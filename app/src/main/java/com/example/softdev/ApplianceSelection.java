package com.example.softdev;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class ApplianceSelection extends AppCompatActivity {

    private RecyclerView recyclerView;

    private List<DataModel> mList;
    private ItemAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appliance_selection);

        Button back2 = findViewById(R.id.btnGoBack2);
        back2.setOnClickListener(v -> auto());

        recyclerView = findViewById(R.id.main_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mList = new ArrayList<>();

        List<String> nestedList1 = new ArrayList<>();
        nestedList1.add("LG-32LM6300");
        nestedList1.add("LG-43UK6500AUA Smart LED TV");
        nestedList1.add("LG-55NANO80UPA");
        nestedList1.add("LG-86UP8770PVA");
        nestedList1.add("LG-OLED65G1PUA");

        nestedList1.add("PANASONIC-LED TV TH-50LX800X");
        nestedList1.add("PANASONIC-LED TV TH-75LX650X");
        nestedList1.add("PANASONIC-TH-TH-65JX600X");
        nestedList1.add("PANASONIC-TH-50JX600X");
        nestedList1.add("PANASONIC-TH-43MS600X");

        nestedList1.add("SONY-XBR-55A9G");
        nestedList1.add("SONY-KD-65X8500G");
        nestedList1.add("SONY-XBR-65A8G");
        nestedList1.add("SONY-XBR-85X900H");
        nestedList1.add("SONY-XBR-75X950H");

        nestedList1.add("SAMSUNG-QN65Q90T");
        nestedList1.add("SAMSUNG-QN55Q60R");
        nestedList1.add("SAMSUNG-QN85Q70T");
        nestedList1.add("SAMSUNG-UN32M4500");
        nestedList1.add("SAMSUNG-UN50NU7100");


        List<String> nestedList2 = new ArrayList<>();
        nestedList2.add("SAMSUNG-RT20FARVDSA");
        nestedList2.add("SAMSUNG-RT22FARBDS9/TC");
        nestedList2.add("SAMSUNG-RT35K5562SL/ME");
        nestedList2.add("SAMSUNG-RT29K5132SLTC");
        nestedList2.add("SAMSUNG-RF59A70T0S9");

        nestedList2.add("TOSHIBA-GR-A28PS");
        nestedList2.add("TOSHIBA-GR-RB410WE");
        nestedList2.add("TOSHIBA-GR-A25PS");
        nestedList2.add("TOSHIBA-GR-RS682WE");
        nestedList2.add("TOSHIBA-GR-RT416WE-PMY");

        List<String> nestedList3 = new ArrayList<>();
        nestedList3.add("SAMSUNG-WW80J54E0BW");
        nestedList3.add("SAMSUNG-WF45R6300AV/US");
        nestedList3.add("SAMSUNG-WA10J5730SS/GU");
        nestedList3.add("SAMSUNG-WD6000J");
        nestedList3.add("SAMSUNG-WA70H4200SW/TL");

        nestedList3.add("TOSHIBA-TW-BJ100M4-IND");
        nestedList3.add("TOSHIBA-AW-DJ1000F-IND");
        nestedList3.add("TOSHIBA-AW-DUK1150H-IND(SK)");
        nestedList3.add("TOSHIBA-AW-M901B-IND(SG)");
        nestedList3.add("TOSHIBA-TW-J80S2-IND");

        List<String> nestedList4 = new ArrayList<>();
        nestedList4.add("HANABISHI-HRC 10SS");
        nestedList4.add("HANABISHI-HRC 18FS");
        nestedList4.add("HANABISHI-HRC 10FS");
        nestedList4.add("HANABISHI-HRC 18D");
        nestedList4.add("HANABISHI-HRC 10D");

        nestedList4.add("IMARFLEX-IRC-150PC");
        nestedList4.add("IMARFLEX-IRC-180");
        nestedList4.add("IMARFLEX-IRJ-3500");
        nestedList4.add("IMARFLEX-IRC-1800");
        nestedList4.add("IMARFLEX-IRC-2500");

        nestedList4.add("KYOWA-KW-2200");
        nestedList4.add("KYOWA-KW-2042");
        nestedList4.add("KYOWA-KW-1810");
        nestedList4.add("KYOWA-KW-2201");
        nestedList4.add("KYOWA-KW-1811");

        nestedList4.add("PANASONIC-SR-JN185");
        nestedList4.add("PANASONIC-SR-DF181");
        nestedList4.add("PANASONIC-SR-G06FGL");
        nestedList4.add("PANASONIC-SR-Y18FHS");
        nestedList4.add("PANASONIC-SR-CEZ18");

        List<String> nestedList5 = new ArrayList<>();
        nestedList5.add("HANABISHI-HAFAN16");
        nestedList5.add("HANABISHI-HAFAN18");
        nestedList5.add("HANABISHI-HAFAN20");
        nestedList5.add("HANABISHI-HAFAN25");
        nestedList5.add("HANABISHI-HAFAN26");

        nestedList5.add("TOUGH MAMA-NTM-2612");
        nestedList5.add("TOUGH MAMA-NTM-1616");
        nestedList5.add("TOUGH MAMA-NTM-1618");
        nestedList5.add("TOUGH MAMA-NTM-1620");
        nestedList5.add("TOUGH MAMA-NTM-1622");



        mList.add(new DataModel(nestedList1, "Television"));
        mList.add(new DataModel(nestedList2,"Refrigerator"));
        mList.add(new DataModel(nestedList3,"Washing Machine"));
        mList.add(new DataModel(nestedList4 ,"Rice Cooker"));
        mList.add(new DataModel(nestedList5,"Electric Fan"));


        adapter = new ItemAdapter(mList);
        recyclerView.setAdapter(adapter);
    }

    public void auto(){
        Intent intent = new Intent(this, automatic.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
