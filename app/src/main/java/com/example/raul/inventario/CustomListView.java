package com.example.raul.inventario;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class CustomListView extends ArrayAdapter<Producto> {

    private ArrayList<String> listaNombres = new ArrayList<String>();
    private ArrayList<Float> listaPrecios  = new ArrayList<Float>();
    private ArrayList<String> listaDescripciones  = new ArrayList<String>();
    private ArrayList<String> listaImagenes = new ArrayList<String>();
    private Activity context;

    public CustomListView(Activity context, ArrayList<Producto> listaProductos){
        super(context, R.layout.lista_productos, listaProductos);

        this.context = context;
        setListaNombres(listaProductos);
        setListaPrecios(listaProductos);
        setListaDescripciones(listaProductos);
        setListaImagenes(listaProductos);
    }

    public ArrayList<String> getListaNombres() {
        return listaNombres;
    }

    public void setListaNombres(ArrayList<Producto> listaNombres) {
        for (int i = 0; i<listaNombres.size(); i++){
            this.listaNombres.add(listaNombres.get(i).getNombre());
        }
    }

    public ArrayList<Float> getListaPrecios() {
        return listaPrecios;
    }

    public void setListaPrecios(ArrayList<Producto> listaPrecios) {
        for (int i = 0; i<listaPrecios.size(); i++){
            this.listaPrecios.add(listaPrecios.get(i).getPrecio());
        }
    }

    public ArrayList<String> getListaDescripciones() {
        return listaDescripciones;
    }

    public void setListaDescripciones(ArrayList<Producto> listaDescripciones) {
        for (int i = 0; i<listaDescripciones.size(); i++){
            this.listaDescripciones.add(listaDescripciones.get(i).getDescripcion());
        }
    }

    public ArrayList<String> getListaImagenes() {
        return listaImagenes;
    }

    public void setListaImagenes(ArrayList<Producto> listaImagenes) {
        for (int i = 0; i<listaImagenes.size(); i++){
            this.listaImagenes.add(listaImagenes.get(i).getImagen());
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder = null;
        if(view == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            view = layoutInflater.inflate(R.layout.lista_productos,null, true);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Bitmap bitmap = null;
        try {
            bitmap = new ImageTask().execute(listaImagenes.get(position)).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        viewHolder.imageView.setImageBitmap(bitmap);
        viewHolder.textView_nombreProducto.setText(this.listaNombres.get(position));
        viewHolder.textView_precioProducto.setText(this.listaPrecios.get(position).toString());

        return view;
    }

    class ViewHolder{
        TextView textView_nombreProducto;
        TextView textView_precioProducto;
        ImageView imageView;

        ViewHolder(View view){
            textView_nombreProducto = view.findViewById(R.id.nombreProducto);
            textView_precioProducto = view.findViewById(R.id.precioProducto);
            imageView = view.findViewById(R.id.imagenProducto);
        }
    }
}
