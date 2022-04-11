package ru.narfu.iislab1.utils

import org.apache.poi.xwpf.extractor.XWPFWordExtractor
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.File

object Word {
	fun readParagraphs(pathName: String): MutableList<String> = pathName
		.let(::File)
		.let(File::inputStream)
		.let(::XWPFDocument)
		.let(::XWPFWordExtractor)
		.use { it.text.lines() }
		.toMutableList()

	fun writeParagraphs(pathName: String, paragraphs: List<String>) {
		val doc = XWPFDocument()
		paragraphs.forEach {
			doc.createParagraph()
				.createRun()
				.setText(it)
		}
		doc.write(File(pathName).outputStream())
	}
}