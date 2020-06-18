package com.dmitriybakunovich.languagelearning.data.repository

import androidx.lifecycle.LiveData
import com.dmitriybakunovich.languagelearning.data.db.DatabaseDao
import com.dmitriybakunovich.languagelearning.data.db.entity.BookData
import com.dmitriybakunovich.languagelearning.data.db.entity.BookWithText
import com.dmitriybakunovich.languagelearning.data.db.entity.TextData

class TextDataRepository(private val databaseDao: DatabaseDao) {
    val allBookWithText: LiveData<List<BookWithText>> = databaseDao.getBookWithText()
    val allBook: LiveData<List<BookData>> = databaseDao.getAllBookData()

    suspend fun getBook(bookData: BookData): List<TextData> =
        databaseDao.getTextBook(bookData.bookName)

    suspend fun insert(textData: TextData) {
        databaseDao.insert(textData)
    }

    suspend fun update(bookData: BookData) {
        databaseDao.update(bookData)
    }

    fun loadFullTextMainBook(bookName: String) =
        "И действительно, хотя одежда у него была плоховата, а речь отличалась грубостью, он не был похож на простого матроса. Скорее его можно было принять за штурмана или шкипера, который привык, чтобы ему подчинялись, и любит давать волю своему кулаку. Человек с тачкой рассказал нам, что незнакомец прибыл вчера утром на почтовых в «Гостиницу короля Георга» и расспрашивал там обо всех постоялых дворах, расположенных поблизости от моря. Должно быть, услышав о нашем трактире хорошие отзывы и узнав, что он стоит на отлете, капитан решил поселиться у нас. Вот и все, что удалось нам узнать о своем постояльце.\n" +
                "\n" +
                "Человек он был молчаливый. Целыми днями бродил по берегу бухты или взбирался на скалы с медной подзорной трубой. По вечерам он сидел в общей комнате в самом углу, у огня, и пил ром, слегка разбавляя его водой. Он не отвечал, если с ним заговаривали. Только окинет свирепым взглядом и засвистит носом, как корабельная сирена в тумане. Вскоре мы и наши посетители научились оставлять его в покое. Каждый день, воротившись с прогулки, он справлялся, не проходили ли по нашей дороге какие-нибудь моряки. Сначала мы думали, что ему не хватает компании таких же забулдыг, как он сам. Но под конец мы стали понимать, что он желает быть подальше от них. Если какой-нибудь моряк, пробираясь по прибрежной дороге в Бристоль, останавливался в «Адмирале Бенбоу», капитан сначала разглядывал его из-за дверной занавески и только после этого выходил в гостиную. В присутствии подобных людей он всегда сидел тихо, как мышь."

    fun loadFullTextChildBook(bookName: String) =
        "Indeed, although his clothes were rather poor and his speech was rude, he did not look like a simple sailor. Rather, he could be mistaken for a navigator or a skipper who was used to being obeyed and likes to give free rein to his fist. A man with a wheelbarrow told us that a stranger arrived at the King George's Hotel postal service yesterday morning and asked about all the inns located near the sea there. It must have been that the captain decided to settle with us after hearing good reviews about our tavern and learning that he was on departure. That's all we managed to find out about our guest.\n" +
                "\n" +
                "He was a silent man. For days on end I wandered along the shore of the bay or climbed the cliffs with a copper telescope. In the evenings, he sat in a common room in the corner, by the fire, and drank rum, slightly diluting it with water. He did not answer if they spoke to him. He will only cast a fierce look and whistle his nose like a ship's siren in the fog. Soon we and our visitors learned to leave him alone. Every day, returning from a walk, he wondered if any sailors had passed along our road. At first we thought that he lacked a company of the same backdoors as he himself. But in the end, we began to understand that he wants to be away from them. If any sailor, making his way along the coastal road to Bristol, stopped at the Admiral Benbow, the captain first looked at him from behind the door curtain and only then went into the living room. In the presence of such people, he always sat quietly, like a mouse."
}