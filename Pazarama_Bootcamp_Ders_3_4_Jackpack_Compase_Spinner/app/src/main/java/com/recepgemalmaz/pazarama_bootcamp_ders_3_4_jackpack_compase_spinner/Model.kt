package com.recepgemalmaz.pazarama_bootcamp_ders_3_4_jackpack_compase_spinner

import android.util.Log
import android.content.ContentValues
import android.content.Context

data class Kategori(var K_ID:Int = 0, var Aciklama:String = "")

data class Parca(
    var P_ID:Int = 0,
    var Kategori_ID:Int = 0,
    var Adi:String = "",
    var StokAdedi:Int = 0,
    var Fiyati:Long = 0L
)

class KategoriRepository(c:Context)
{
    private var context:Context? = null
    private var dbg:DBGateway? = null

    init {
        context = c
        dbg = DBGateway(c)
    }

    public fun KategorileriOlustur()
    {
        var db = dbg!!.writableDatabase

        var cur = db.rawQuery("Select * from Kategoriler", null)

        if (cur.count == 0) {
            var cv = ContentValues()
            cv.put("Aciklama", "Motor")
            db.insert("Kategoriler", null, cv)

            cv = ContentValues()
            cv.put("Aciklama", "Kaporta")
            db.insert("Kategoriler", null, cv)

            cv = ContentValues()
            cv.put("Aciklama", "Elektrik")
            db.insert("Kategoriler", null, cv)

            cv = ContentValues()
            cv.put("Aciklama", "Aksesuar")
            db.insert("Kategoriler", null, cv)
        }

        db.close()
    }

    public fun GetKategoriler() : MutableList<Kategori>
    {
        var db = dbg!!.readableDatabase

        var cur = db.rawQuery("Select * from Kategoriler", null)

        var lst = mutableListOf<Kategori>()

        while (cur.moveToNext())
        {
            lst.add(
                Kategori(
                    cur.getInt(0),
                    cur.getString(1)))
        }

        return lst
    }
}

class ParcaRepository(c:Context)
{
    private var context:Context? = null
    private var dbg:DBGateway? = null

    init {
        context = c
        dbg = DBGateway(c)
    }

    fun ParcaEkle(p:Parca)
    {
        var db = dbg!!.writableDatabase

        var cv = ContentValues()
        cv.put("KategoriID", p.Kategori_ID)
        cv.put("Adi", p.Adi)
        cv.put("StokAdedi", p.StokAdedi)
        cv.put("Fiyati", p.Fiyati)

        db.insert("Parcalar", null, cv)

        db.close()
    }

    fun ParcalariOlustur(p_id: Int,kid: Int, adi: String, stokAdedi: Int, fiyati: Long)
    {
        ParcaEkle(Parca(p_id, kid, adi, stokAdedi, fiyati))
    }

    
    fun parcaSil(p_id:Int)
    {
        var db = dbg!!.writableDatabase

        db.delete("Parcalar", "ID = ?", arrayOf(p_id.toString()))

        db.close()
    }

    fun ParcalarByKategoriID(kid:Int) : MutableList<Parca>
    {
        var lst = mutableListOf<Parca>()

        var db = dbg!!.readableDatabase

        var cursor =  db.rawQuery("Select * from Parcalar Where KategoriID = ?", arrayOf(kid.toString()))

        while (cursor.moveToNext())
        {
            lst.add(
                Parca(cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getLong(4)
                )
            )
        }

        db.close()
        return lst
    }
}

/*
fun main()
{
    val veriler = Veriler()
    val parcalar = veriler.GetParcalarByKategoriID(1)
    println("Parça sayısı: ${parcalar.size}")
    parcalar.forEach {
        println("Parça Adı: ${it.Adi}")
        println("Parça Fiyatı: ${it.Fiyati}")
    }

}*/
