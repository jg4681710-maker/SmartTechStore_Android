package com.smarttech.store.roles;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.smarttech.store.MainActivity;
import com.smarttech.store.buyer.CatalogActivity;

/** Role-based workspace for the academic prototype. Data actions are demonstrative/local. */
public class RoleWorkspaceActivity extends Activity {
    private String role, email;
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        role=getIntent().getStringExtra("USER_ROLE"); email=getIntent().getStringExtra("USER_EMAIL");
        if(role==null) role="Vendedor";
        ScrollView scroll=new ScrollView(this);
        LinearLayout root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(28,32,28,28); root.setBackgroundColor(Color.rgb(245,247,250)); scroll.addView(root);
        TextView title=new TextView(this); title.setText("SmartTech Store\n"+role); title.setTextSize(27); title.setTextColor(Color.rgb(20,45,75)); title.setGravity(Gravity.CENTER); title.setPadding(0,0,0,12); root.addView(title);
        TextView welcome=new TextView(this); welcome.setText("Sesión: "+(email==null?"usuario":email)+"\nPanel de gestión"); welcome.setTextSize(15); welcome.setTextColor(Color.DKGRAY); root.addView(welcome);
        if(role.equalsIgnoreCase("Administrador")) {
            section(root,"Administración"); action(root,"Gestión de usuarios","Crear, consultar, editar y eliminar usuarios (demo)"); action(root,"Gestión de productos","Administrar catálogo, precio y disponibilidad (demo)"); action(root,"Reporte de ventas","Resumen de ventas y pedidos registrados (demo)");
        } else {
            section(root,"Zona de vendedor"); action(root,"Mis productos","Consultar y administrar productos publicados (demo)"); action(root,"Pedidos recibidos","Historial de pedidos y estados (demo)"); action(root,"Mi perfil","Información de cuenta del vendedor (demo)");
        }
        Button logout=new Button(this); logout.setText("Cerrar sesión"); logout.setOnClickListener(v->{Intent i=new Intent(this, MainActivity.class); i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK); startActivity(i); finish();}); root.addView(logout);
        setContentView(scroll);
    }
    private void section(LinearLayout root,String text){TextView t=new TextView(this);t.setText(text);t.setTextSize(20);t.setTextColor(Color.rgb(25,90,140));t.setPadding(0,26,0,8);root.addView(t);}
    private void action(LinearLayout root,String title,String detail){Button b=new Button(this);b.setText(title+"\n"+detail);b.setAllCaps(false);b.setGravity(Gravity.CENTER_VERTICAL);b.setOnClickListener(v->{if(title.equals("Mis productos")||title.equals("Gestión de productos")){Intent i=new Intent(this,CatalogActivity.class);i.putExtra("USER_ROLE",role);startActivity(i);}else Toast.makeText(this,detail,Toast.LENGTH_LONG).show();});root.addView(b);}
}
