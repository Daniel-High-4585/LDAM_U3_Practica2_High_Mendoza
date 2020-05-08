package mx.edu.ittepic.ldam_u3_practica2_high_mendoza

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main3.*

class Main3Activity : AppCompatActivity() {

    var baseRemota = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        btnBuscar.setOnClickListener {
            if (txtBuscar.text.isEmpty()){
                Toast.makeText(this,"debes poner un valor para buscar", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            realizarConsulta(txtBuscar.text.toString(),cmbBusqueda.selectedItemPosition)
        }

        btnCerrar.setOnClickListener {
            finish()
        }
    }

    private fun realizarConsulta(valor: String, clave: Int) {
        /*
            0: Nombre
            1: Producto
            2: Domicilio
            3: Precio
        */
        when(clave){
            0->{consultaNombre(valor)}
            1->{consultaProducto(valor)}
            2->{consultaDomicilio(valor)}
            3->{consultaPrecio(valor.toDouble())}
        }
    }

    private fun consultaNombre(valor: String) {
        baseRemota.collection("restaurante").whereEqualTo("nombre",valor)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if(firebaseFirestoreException!=null){
                    txtConsulta.setText("Error, no hay conexion")
                    return@addSnapshotListener
                }

                var res =""

                for(document in querySnapshot!!){
                    var smn = "Nelson"
                    if(document.get("producto.entregado")==true){
                        smn="Simon"
                    }
                    res +="id: "+document.id+"\nNombre: "+document.getString("nombre")+
                            "\nDomicilio: "+document.getString("domicilio")+"\nTelefono: "+
                            document.getString("celular")+"\nProducto: "+document.get("producto.descripcion")+
                            "\nCantidad: "+document.get("producto.cantidad")+"\nCosto: $"+document.get("producto.precio")+
                            "\nEntregado: "+smn
                }
                txtConsulta.setText(res)
            }
    }

    private fun consultaProducto(valor: String) {

    }

    private fun consultaDomicilio(valor: String) {

    }

    private fun consultaPrecio(valor: Double) {

    }
}
