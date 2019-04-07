package fr.gsb.rv;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.gsb.rv.entites.Mois;
import fr.gsb.rv.entites.Motif;
import fr.gsb.rv.entites.Praticien;
import fr.gsb.rv.entites.RapportVisite;
import fr.gsb.rv.modeles.ModeleGsb;
import fr.gsb.rv.technique.Session;

public class ConsultRvActivity extends AppCompatActivity{

    private static List<Integer> lesAnnees = new ArrayList<Integer>();
    Spinner spMois;
    Spinner spAnnees;
    protected List<RapportVisite> lesRvs = new ArrayList<RapportVisite>() ;
    ListView lvRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultrv);
        spMois = (Spinner) findViewById(R.id.mois);
        spAnnees = (Spinner) findViewById(R.id.annees);
        lvRv = (ListView) findViewById(R.id.lvRv);
        int annee = 2000;
        while(annee < 2030){
            lesAnnees.add(annee);
            annee = annee + 1;
        }
        ArrayAdapter<Integer> aaAnnees = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, lesAnnees );
        ConsultRvActivity.ItemMoisAdaptateur itemMois = new ConsultRvActivity.ItemMoisAdaptateur();
        spMois.setAdapter(itemMois);
        aaAnnees.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAnnees.setAdapter(aaAnnees);
        Button btnRetour = (Button) findViewById(R.id.btnRetour);
        Button btnConsult = (Button) findViewById(R.id.btnConsulterRv);
        View.OnClickListener select = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomMois = spMois.getSelectedItem().toString();
                int numMois = Mois.valueOf(nomMois).ordinal();
                int annee = spAnnees.getSelectedItem().hashCode();
                showRv(numMois + 1, annee);
            }
        };
        View.OnClickListener quitter = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentionEnvoyer = new Intent(ConsultRvActivity.this, MenuRvActivity.class);
                startActivity(intentionEnvoyer);
            }
        };
        lvRv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RapportVisite leRv = lesRvs.get(position);
                LayoutInflater factory = LayoutInflater.from(ConsultRvActivity.this);
                final View body = factory.inflate(R.layout.dialog_rv, null);
                TextView tvPra = (TextView) body.findViewById(R.id.tvPra);
                tvPra.setText(leRv.getPraticien().getNom().toUpperCase()+" "+leRv.getPraticien().getPrenom());
                TextView tvBilan = (TextView) body.findViewById(R.id.tvBilan);
                tvBilan.setText(leRv.getBilan());
                TextView tvMotif = (TextView) body.findViewById(R.id.tvMotif);
                tvMotif.setText(leRv.getMotif().getLibelle());
                TextView tvVisite = (TextView) body.findViewById(R.id.tvVisite);
                tvVisite.setText(leRv.getVisite().toString());
                TextView tvRedac = (TextView) body.findViewById(R.id.tvRedac);
                tvRedac.setText(leRv.getRedaction().toString());
                TextView tvLu = (TextView) body.findViewById(R.id.tvLu);
                if(leRv.isLu()){
                    tvLu.setText("Lu");
                }
                else{
                    tvLu.setText("Pas lu");
                }
                AlertDialog.Builder dialog = new AlertDialog.Builder(ConsultRvActivity.this);
                dialog.setTitle("Rapport de Visite n°"+leRv.getNum());
                dialog.setView(body);
                dialog.show();
            }
        });
        btnConsult.setOnClickListener(select);
        btnRetour.setOnClickListener(quitter);
    }

    protected void showRv(int mois, int annee){
        lesRvs.clear();
        String url = ModeleGsb.URL+"rapports/"+Session.getSession().getLeVisiteur().getMatricule()+"/"+mois+"/"+annee; //URL HTTP
        System.out.println(url);
        Response.Listener<JSONArray> ecouteurReponse = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("RV","Rép HTTP") ;
                Log.i("RV","Rép HTTP : " + response.length()) ;
                try{
                    for(int i=0 ; i < response.length() ; i++){
                        RapportVisite unRv = new RapportVisite();
                        String visite = (response.getJSONObject(i).getString("rap_date_visite"));
                        try {
                            Date laVisite = new SimpleDateFormat("yyyy-MM-dd").parse(visite);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                unRv.setVisite(laVisite.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String redac = (response.getJSONObject(i).getString("rap_date_redaction"));
                        try {
                            Date laRedac = new SimpleDateFormat("yyyy-MM-dd").parse(redac);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                unRv.setRedaction(laRedac.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Motif mo = new Motif();
                        mo.setLibelle(response.getJSONObject(i).getString("mo_libelle"));
                        mo.setCode(response.getJSONObject(i).getInt("mo_code"));
                        unRv.setMotif(mo);
                        Praticien lePraticien = new Praticien();
                        lePraticien.setNom(response.getJSONObject(i).getString("pra_nom"));
                        lePraticien.setPrenom(response.getJSONObject(i).getString("pra_prenom"));
                        lePraticien.setVille(response.getJSONObject(i).getString("pra_ville"));
                        unRv.setPraticien(lePraticien);
                        unRv.setBilan(response.getJSONObject(i).getString("rap_bilan"));
                        unRv.setNum(response.getJSONObject(i).getInt("rap_num"));
                        unRv.setLu(response.getJSONObject(i).getBoolean("rap_lu"));
                        ConsultRvActivity.this.lesRvs.add(unRv);
                    }
                    System.out.println("sizeeeeeeeeeeeee"+lesRvs.size());
                    ConsultRvActivity.ItemRvAdaptateur itemRv = new ConsultRvActivity.ItemRvAdaptateur();
                    lvRv.setAdapter(itemRv);
                } catch (JSONException e) {
                    Log.e("APP-RV", "Erreur JSON : "+ e.getMessage());
                }
            }
        }; //Reponse Listener
        Response.ErrorListener ecouteurErreur = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("APP-RV", "Erreur HTTP : "+ error.getMessage());
            }
        }; //Error Listener
        JsonArrayRequest requete = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                ecouteurReponse,
                ecouteurErreur
        );
        RequestQueue fileRequetes = Volley.newRequestQueue(this);
        fileRequetes.add(requete);
    }

    class ItemMoisAdaptateur extends ArrayAdapter<Mois>{

        ItemMoisAdaptateur(){
            super(
                    ConsultRvActivity.this,
                    R.layout.item_mois,
                    R.id.mois,
                    Mois.values()
            );
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View vItem = super.getView(position, convertView, parent);
            TextView tvMois = (TextView) vItem.findViewById(R.id.mois);
            tvMois.setText(Mois.values()[position].toString());
            tvMois.setTextColor(R.color.dark);
            return vItem;
        }

    }

    class ItemRvAdaptateur extends ArrayAdapter<RapportVisite>{

        ItemRvAdaptateur(){
            super(
                    ConsultRvActivity.this,
                    R.layout.item_rv,
                    R.id.tvPraticien,
                    lesRvs
            );
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View vItem = super.getView(position, convertView, parent);
            TextView tvVisite = (TextView) vItem.findViewById(R.id.tvDateVisite);
            tvVisite.setText(" : "+lesRvs.get(position).getVisite().toString());
            return vItem;
        }

    }

}
