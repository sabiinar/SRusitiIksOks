package rs.ac.pr.ftn.srusitiiksoks

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var etInput: EditText
    private lateinit var btnRed: Button
    private lateinit var btnBlue: Button
    private lateinit var btnNewGame: Button
    private lateinit var fields: List<TextView>
    private val occupiedFields = mutableMapOf<Int, Int>() // <Index, Color>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()

        // Враћање стања при ротацији
        savedInstanceState?.let {
            val keys = it.getIntArray("occupiedKeys")
            val values = it.getIntArray("occupiedValues")
            if (keys != null && values != null) {
                for (i in keys.indices) {
                    occupiedFields[keys[i]] = values[i]
                    fields[keys[i]].setBackgroundColor(values[i])
                }
            }
        }

        btnRed.setOnClickListener { colorField(Color.RED) }
        btnBlue.setOnClickListener { colorField(ContextCompat.getColor(this, R.color.blue)) }
        btnNewGame.setOnClickListener { resetGame() }
    }

    private fun initViews() {
        etInput = findViewById(R.id.etInput)
        btnRed = findViewById(R.id.btnRed)
        btnBlue = findViewById(R.id.btnBlue)
        btnNewGame = findViewById(R.id.btnNewGame)

        fields = listOf(
            findViewById(R.id.p1), findViewById(R.id.p2), findViewById(R.id.p3),
            findViewById(R.id.p4), findViewById(R.id.p5), findViewById(R.id.p6),
            findViewById(R.id.p7), findViewById(R.id.p8), findViewById(R.id.p9)
        )
    }

    private fun colorField(color: Int) {
        val inputText = etInput.text.toString().trim()
        if (inputText.isEmpty()) {
            showToast("Унеси број поља!")
            return
        }

        val input = inputText.toIntOrNull() ?: run {
            showToast("Неисправан број!")
            return
        }

        if (input !in 1..9) {
            showToast("Број мора бити између 1 и 9!")
            return
        }

        val index = input - 1

        if (occupiedFields.containsKey(index)) {
            showToast("Поље је већ заузето!")
            return
        }

        fields[index].setBackgroundColor(color)
        occupiedFields[index] = color
    }

    private fun resetGame() {
        fields.forEach {
            it.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        }
        etInput.text.clear()
        occupiedFields.clear()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntArray("occupiedKeys", occupiedFields.keys.toIntArray())
        outState.putIntArray("occupiedValues", occupiedFields.values.toIntArray())
    }
}