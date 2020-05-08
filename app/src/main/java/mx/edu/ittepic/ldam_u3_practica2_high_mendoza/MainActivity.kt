package mx.edu.ittepic.ldam_u3_practica2_high_mendoza

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var baseRemota = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnInsertar.setOnClickListener {
            insertar()
        }

        btnCancelar.setOnClickListener {
            finish()
        }
    }

    private fun insertar() {
        var data = hashMapOf(
            "nombre" to txtNombre.text.toString(),
            "domicilio" to txtDomicilio.text.toString(),
            "celular" to txtCelular.text.toString(),
            "producto" to hashMapOf(
                "descripcion" to txtProducto.text.toString(),
                "precio" to txtPrecio.text.toString().toDouble(),
                "cantidad" to txtCantidad.text.toString().toInt(),
                "entregado" to chkEntregado.isChecked
            )
        )
        baseRemota.collection("restaurante").add(data)
            .addOnSuccessListener {
                Toast.makeText(this, "Se capturo la Info con exito", Toast.LENGTH_LONG).show()
                txtNombre.setText("")
                txtDomicilio.setText("")
                txtCelular.setText("")
                txtProducto.setText("")
                txtPrecio.setText("")
                txtCantidad.setText("")
                chkEntregado.isChecked=false
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error, no se capturo",Toast.LENGTH_LONG).show()
            }

    }
}
