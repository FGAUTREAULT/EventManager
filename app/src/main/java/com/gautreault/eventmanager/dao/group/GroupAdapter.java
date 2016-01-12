package com.gautreault.eventmanager.dao.group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tutos.perso.myapplication.R;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class GroupAdapter extends BaseAdapter {

    private String[] params;
    private LayoutInflater mInflater;

    static class ViewHolder {
        public TextView mNom;
    }


    public GroupAdapter(String[] params, Context context) {
        this.params = params;
        this.mInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Récupérer un item de la liste en fonction de sa position
     *
     * @param position - Position de l'item à récupérer
     * @return l'item récupéré
     */
    public Object getItem(int position) {
        return params[position];
    }

    @Override
    public int getCount() {
        return params.length;
    }

    /**
     * Recuperer l'identifiant d'un item de la liste en fonction de sa position
     *
     * @param position - Position de l item a recuperer
     * @return l identifiant de l item
     */
    public long getItemId(int position) {
        return position;
    }

    /**
     * @param position    est la position de l item dans la liste (et donc dans l'adaptateur).
     * @param convertView est null au depart jusqu au moment ou une partie de la vue n est plus rendu (optimisation)
     * @param parent      est le layout auquel rattacher la vue.
     * @return la vue
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        //Optimisation : utiliser le LayoutInflater afin de ne plus rendre les vues qui ne sont plus affichees
        //LayoutInflater getSystemService (LAYOUT_INFLATER_SERVICE) sur une activité
        //LayoutInflater getLayoutInflater () sur une activité
        //LayoutInflater LayoutInflater.from(Context contexte), sachant que Activity dérive de Context.

        //Optimisation 2 : pattern view holder
        //Dans notre adaptateur, on remarque qu 'on a optimisé le layout de chaque contact en ne l'inflatant que quand c
        //'est nécessaire… mais on inflate quand même les trois vues qui ont le même layout ! C'
        //est moins grave, parce que les vues inflatées par findViewById le sont plus rapidement, mais
        //quand même.Il existe une alternative pour améliorer encore le rendu.Il faut utiliser une
        //classe interne statique, qu 'on appelle ViewHolder d' habitude.Cette classe devra contenir
        //toutes les vues de notre layout:

        //Ensuite, la première fois qu'on
        //inflate le layout, on récupère chaque vue pour les mettre dans le ViewHolder, puis on
        //insère le ViewHolder dans le layout à l'aide de la méthode
        //void setTag (Object tag),qui peut être utilisée sur n
        //'importe quel View. Cette technique permet d' insérer dans notre vue des objets afin de
        //les récupérer plus tard avec la méthode Object getTag().On récupérera le ViewHolder si
        //le convertView n 'est pas null, comme ça on n' aura inflaté les vues qu 'une fois chacune.


        ViewHolder holder = null;
        // Si la vue n'est pas recyclée
        if (convertView == null) {
            // On récupère le layout
            convertView = mInflater.inflate(R.layout.database_cursor_row, null);

            holder = new ViewHolder();
            // On place les widgets de notre layout dans le holder
            holder.mNom = (TextView) convertView.findViewById(R.id.cursorName);

            // puis on insère le holder en tant que tag dans le layout
            convertView.setTag(holder);
        } else {
            // Si on recycle la vue, on récupère son holder en tag
            holder = (ViewHolder) convertView.getTag();
        }

        // Dans tous les cas, on récupère le contact téléphonique concerné
        String c = (String) getItem(position);
        // Si cet élément existe vraiment…
        // On place dans le holder les informations sur le contact
        if (c != null) holder.mNom.setText(c);
        return convertView;
    }
}
