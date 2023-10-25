package com.recepgemalmaz.pazarama_bootcamp_ders_3_4_jackpack_compase_spinner

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

import com.recepgemalmaz.pazarama_bootcamp_ders_3_4_jackpack_compase_spinner.ui.theme.Pazarama_Bootcamp_Ders_3_4_Jackpack_Compase_SpinnerTheme

class ParcaEkleActivity : ComponentActivity() {
    private lateinit var parcaRepository: ParcaRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parcaRepository = ParcaRepository(this)
        val kategoriID = intent.getIntExtra("kategoriID", 1)

        setContent {
            Pazarama_Bootcamp_Ders_3_4_Jackpack_Compase_SpinnerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ParcaEkleScreen(kategoriID)
                }
            }
        }
    }

    @Composable
    fun ParcaEkleScreen(gelenKategori: Int) {
        var kategoriID by remember { mutableStateOf(gelenKategori) }
        var parcaAdi by remember { mutableStateOf("") }
        var stokAdedi by remember { mutableStateOf(0) }
        var fiyat by remember { mutableStateOf(0L) }

        Column(
            modifier = Modifier.fillMaxSize().background(Color.Cyan).padding(bottom = 45.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,



        ) {
            Text(
                text = "Parça Ekleme",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(1.dp)
                ,color = Color.Red
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text =  KategoriRepository(this@ParcaEkleActivity).GetKategoriler().find { it.K_ID == gelenKategori }!!.Aciklama + " Kategorisi",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
            // Kategori Seçimi get intent ile belli
            //val Gelenkategori = parcaRepository.ParcalarByKategoriID(gelenKategori)

            Text(
                text = "Parça Adı",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )
            BasicTextField(
                value = parcaAdi,
                onValueChange = { parcaAdi = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(1.dp).background(Color.LightGray).size(50.dp),
                textStyle = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Stok Adedi",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )
            BasicTextField(
                value = stokAdedi.toString(),
                onValueChange = { stokAdedi = it.toIntOrNull() ?: 0 },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(1.dp).background(Color.LightGray).size(40.dp),
                textStyle = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Fiyatı (TL)",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )
            BasicTextField(
                value = fiyat.toString(),
                onValueChange = { fiyat = it.toLongOrNull() ?: 0L },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(1.dp).background(Color.LightGray).size(40.dp),
                textStyle = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val yeniParca = Parca(
                        Kategori_ID = kategoriID,
                        Adi = parcaAdi,
                        StokAdedi = stokAdedi,
                        Fiyati = fiyat
                    )
                    parcaRepository.ParcaEkle(yeniParca)
                    val intent = Intent(this@ParcaEkleActivity, MainActivity::class.java)
                    startActivity(intent)
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(  text = KategoriRepository(this@ParcaEkleActivity).GetKategoriler().find { it.K_ID == gelenKategori }!!.Aciklama + " Kategorisine Parca Ekle")
            }
        }
    }
}