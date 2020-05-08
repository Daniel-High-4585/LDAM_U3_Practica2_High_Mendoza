package mx.edu.ittepic.ldam_u3_practica2_high_mendoza

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {

    var baseRemota = FirebaseFirestore.getInstance()
    var dataLista = ArrayList<String>()
    var listaId = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        mostrarLista()

        btnNPedido.setOnClickListener {
             var v = Intent(this,MainActivity::class.java)
            startActivity(v)
        }

        btnConsultar.setOnClickListener {
            var v = Intent(this,Main3Activity::class.java)
            startActivity(v)
        }

        lista.setOnItemClickListener { parent, view, position, id ->
            if (listaId.size == 0) {
                return@setOnItemClickListener
            }
            alertaEliminarActualizar(position)
        }

    }

    private fun mostrarLista() {
        baseRemota.collection("restaurante").addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {//si es diferente de null, entonce hay un error
                Toast.makeText(this, "No se puede acceder a consulta", Toast.LENGTH_LONG).show()
                return@addSnapshotListener
            }

            dataLista.clear()
            listaId.clear()

            for (document in querySnapshot!!) {
                var smn = "Nelson"
                if(document.get("producto.entregado")==true){
                    smn="Simon"
                }
                var cadena = "Nombre: "+document.getString("nombre") + "\n" +
                             "Domicilio: "+document.getString("domicilio") +"\n"+
                             "Telefono: "+document.getString("celular")+"\n"+
                             "Producto: "+document.get("producto.descripcion")+"\n"+
                             "Cantidad: "+document.get("producto.cantidad")+"\n"+
                             "Costo: $"+document.get("producto.precio")+"\n"+
                             "Entregado? "+smn
                dataLista.add(cadena)//agregar los datos a la cadena
                listaId.add(document.id)//agregar la id del documento
            }

            if (dataLista.size == 0) {
                dataLista.add("No Hay nada")
            }

            var adaptador = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,dataLista)
            lista.adapter = adaptador
        }
    }

    private fun alertaEliminarActualizar(position: Int) {
        AlertDialog.Builder(this).setTitle("Atencion")
            .setMessage("Que deseas hacer con: \n\n${dataLista[position]} ")
            .setPositiveButton("Eliminar"){d,w->
                eliminar(listaId[position])
            }
            .setNegativeButton("Actualizar"){d,w->
                llamarVentanaActualizar(listaId[position])
            }
            .setNeutralButton("Cancelar"){dialog, which ->  }
            .show()
    }

    private fun eliminar(idEliminar: String) {
        baseRemota.collection("restaurante")
            .document(idEliminar)
            .delete().addOnSuccessListener {
                Toast.makeText(this,"se elimino con exito",Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(this,"se se pudo eliminar",Toast.LENGTH_LONG).show()
            }
    }

    private fun llamarVentanaActualizar(idActualizar: String) {
        baseRemota.collection("restaurante").document(idActualizar).get()//recupera la informacion
            .addOnSuccessListener {

                var v = Intent(this,Main4Activity::class.java)
                v.putExtra("id",idActualizar)                               //pasar vars al otro activity
                v.putExtra("nombre",it.getString("nombre"))
                v.putExtra("domicilio",it.getString("domicilio"))
                v.putExtra("celular",it.getString("celular"))
                v.putExtra("descripcion",it.getString("producto.descripcion"))
                v.putExtra("cantidad",it.getDouble("producto.cantidad"))
                v.putExtra("precio",it.getDouble("producto.precio"))
                v.putExtra("entregado",it.getBoolean("producto.entregado"))

                startActivity(v)
            }
            .addOnFailureListener {
                Toast.makeText(this,"Error: No hay conexion con la base de datos",Toast.LENGTH_LONG).show()
            }
    }

}
