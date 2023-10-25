package com.recepgemalmaz.pazarama_bootcamp_ders_3_4_jackpack_compase_spinner


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.recepgemalmaz.pazarama_bootcamp_ders_3_4_jackpack_compase_spinner.ui.theme.Pazarama_Bootcamp_Ders_3_4_Jackpack_Compase_SpinnerTheme




var KategoriIDValue = 1

class MainActivity : ComponentActivity() {
    private lateinit var kategoriRepository: KategoriRepository
    private lateinit var parcaRepository: ParcaRepository
    private lateinit var kategorikParcalar:MutableList<Parca>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        kategoriRepository = KategoriRepository(this)
        parcaRepository = ParcaRepository(this)

        kategoriRepository.KategorileriOlustur()

        setContent {
            Pazarama_Bootcamp_Ders_3_4_Jackpack_Compase_SpinnerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                )
                {
                    kategorikParcalar = parcaRepository.ParcalarByKategoriID(KategoriIDValue)
                    MainScreen()
                    Log.e("hop", KategoriIDValue.toString())
                }
            }
        }
    }




    @Composable
    fun MainScreen()
    {
        val kategoriler = kategoriRepository.GetKategoriler()

        var expanded = remember { mutableStateOf(false) }
        var secilenKategori = remember { mutableStateOf(kategoriler[KategoriIDValue-1] )  }

        Column(
            Modifier
                .fillMaxSize()
                .background(Color.Cyan).padding(bottom = 45.dp),

            Arrangement.Top, Alignment.Start
        )
        {
            Box(modifier = Modifier
                .fillMaxWidth()
                .background(Color.Magenta)
                .height(35.dp))
            {
                Row(
                    Modifier
                        .clickable {
                            expanded.value = !expanded.value
                        }
                        .align(Alignment.TopStart))
                {
                    Text(text = secilenKategori.value.Aciklama)
                    Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
                }
                DropdownMenu(expanded = expanded.value,
                    onDismissRequest = {  expanded.value = false })
                {
                    kategoriler.forEach {

                        DropdownMenuItem(
                            onClick = {
                                secilenKategori.value = it

                                expanded.value = false

                                kategorikParcalar = parcaRepository.ParcalarByKategoriID(secilenKategori.value.K_ID)
                            },text =
                            {
                                Text(text = it.Aciklama )
                            })
                    }
                }
            }
            ParcaListesiGoster(lst = kategorikParcalar, secilenKategori = secilenKategori)
            Spacer(modifier = Modifier.weight(1f))

        }

        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    val intent = Intent(this@MainActivity, ParcaEkleActivity::class.java).apply{
                        putExtra("kategoriID", secilenKategori.value.K_ID)
                        KategoriIDValue = secilenKategori.value.K_ID
                    }
                    startActivity(intent)
                }
            ) {
                Text(text = "Parca Ekle")
            }
        }
    }

    @Composable
    fun ParcaGoster(p: Parca, onDeleteClick: () -> Unit) {
        var ctx = LocalContext.current

        Column(modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
        ) {
            Text(text = p.Adi, style = MaterialTheme.typography.headlineMedium)
            Spacer(
                Modifier
                    .padding(1.dp)
                    .fillMaxWidth(0.7F)
                    .background(Color.DarkGray)
                    .height(0.5.dp)
            )
            Text(text = "Stok Adedi: ${p.StokAdedi}", style = MaterialTheme.typography.bodyMedium)
            Spacer(
                Modifier
                    .padding(1.dp)
                    .fillMaxWidth(0.7F)
                    .background(Color.DarkGray)
                    .height(0.5.dp)
            )
            Text(text = "Fiyatı: ${p.Fiyati} TL", style = MaterialTheme.typography.bodyMedium)
            Spacer(
                Modifier
                    .padding(1.dp)
                    .fillMaxWidth(0.7F)
                    .background(Color.Red)
                    .height(1.5.dp)
            )

            // Carpi butonu ekleniyor
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Sil"
                )
            }
        }
    }


    @Composable
    fun ParcaListesiGoster(lst:List<Parca>, secilenKategori: MutableState<Kategori>)
    {
        LazyColumn(Modifier.border(3.dp, Color.Blue),
            contentPadding = PaddingValues(5.dp),
            verticalArrangement = Arrangement.Bottom,
            userScrollEnabled = true
        )
        {
            this.items(lst)
            {
                ParcaGoster(p = it, onDeleteClick = {
                    parcaRepository.parcaSil(it.P_ID)
                    kategorikParcalar = parcaRepository.ParcalarByKategoriID(it.Kategori_ID)
                    //sildikten sonra ekranin yenilenmesi
                    setContent {
                        Pazarama_Bootcamp_Ders_3_4_Jackpack_Compase_SpinnerTheme {
                            // A surface container using the 'background' color from the theme
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                color = MaterialTheme.colorScheme.background
                            )
                            {
                                kategorikParcalar = parcaRepository.ParcalarByKategoriID(secilenKategori.value.K_ID)
                                MainScreen()
                            }
                        }
                    }
                })
            }
            item {
                Spacer(modifier = Modifier.height(50.dp)) // 50dp boşluk bırak
            }
        }
    }
}
