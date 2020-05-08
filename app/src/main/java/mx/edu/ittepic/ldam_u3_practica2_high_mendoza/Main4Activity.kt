package mx.edu.ittepic.ldam_u3_practica2_high_mendoza

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main4.*

class Main4Activity : AppCompatActivity() {

    var id =""
    var basedatos = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)

        var extras = intent.extras

        if (extras != null) {
            id = extras.getString("id").toString()
        }

        upNombre.setText(extras?.getString("nombre"))
        upDomicilio.setText(extras?.getString("domicilio"))
        upCelular.setText(extras?.getString("celular"))
        upProducto.setText(extras?.getString("descripcion"))
        upPrecio.setText(extras?.getDouble("precio").toString())
        upCantidad.setText(extras?.getDouble("cantidad").toString())

        if(extras?.getBoolean("entregado")==true){
            chkEntregadoUp.isChecked=true
        }


        btnActualizar.setOnClickListener{
            actualizar()
        }


        btnCancelarUp.setOnClickListener {
            finish()
        }

    }

    private fun actualizar() {

        var entregado = false

        if(chkEntregadoUp.isChecked){
            entregado = true
        }

        basedatos.collection("restaurante")
            .document(id).update(
                "nombre",upNombre.text.toString(),
                "domicilio",upDomicilio.text.toString(),
                "celular",upCelular.text.toString(),
                "producto.descripcion",upProducto.text.toString(),
                "producto.precio",upPrecio.text.toString().toDouble(),
                "producto.cantidad",upCantidad.text.toString().toDouble(),
                "producto.entregado",entregado
            )
            .addOnSuccessListener {
                Toast.makeText(this,"Actualizacion Realizada con exito", Toast.LENGTH_LONG).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this,"Error: No se puede actualizar", Toast.LENGTH_LONG).show()
            }
    }
}
