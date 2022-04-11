package ru.narfu.iislab1

import ru.narfu.iislab1.utils.Dadata
import ru.narfu.iislab1.utils.Word

private const val COMPANY_WILDCARD = "\${c}"
private const val ADDRESS_WILDCARD = "\${a}"

fun main() {
	println("Введите путь к Word документу:")
	val pathName = readln()

	val paragraphs = Word.readParagraphs(pathName)

	paragraphs.indices.forEach { index ->
		while (COMPANY_WILDCARD in paragraphs[index]) {
			println(paragraphs[index].replaceFirst(COMPANY_WILDCARD, "<имя компании>"))
			print("Введите название компании и нажмите Enter: ")
			insertCompany(paragraphs, index, readln())
		}
		while (ADDRESS_WILDCARD in paragraphs[index]) {
			println(paragraphs[index].replaceFirst(ADDRESS_WILDCARD, "<адрес>"))
			print("Введите адрес и нажмите Enter: ")
			insertAddress(paragraphs, index, readln())
		}
	}
	Word.writeParagraphs(pathName, paragraphs)
}

private tailrec fun insertCompany(paragraphs: MutableList<String>, index: Int, companyNameTemplate: String) {
	val companies = Dadata.getCompanies(companyNameTemplate)
		.onEachIndexed { ind, company ->
			println("[${ind.inc()}] ${company.fullName}")
		}
	print("Введите цифру с подходящей компанией, либо уточните название: ")
	val readLn = readln()
	println()
	when (val answer: Any = readLn.toIntOrNull() ?: readLn) {
		is Int -> {
			val companyData = companies[answer.dec()].toString()
			paragraphs[index] = paragraphs[index].replaceFirst(COMPANY_WILDCARD, companyData)
		}
		is String -> {
			insertCompany(paragraphs, index, answer)
		}
	}
}

private tailrec fun insertAddress(paragraphs: MutableList<String>, index: Int, addressTemplate: String) {
	val addresses = Dadata.getAddresses(addressTemplate)
		.onEachIndexed { ind, address ->
			println("[${ind.inc()}] ${address.shortAddress}")
		}
	print("Введите цифру с подходящим адресом, либо уточните название: ")
	val readLn = readln()
	println()
	when (val answer: Any = readLn.toIntOrNull() ?: readLn) {
		is Int -> {
			val address = addresses[answer.dec()].fullAddress
			paragraphs[index] = paragraphs[index].replaceFirst(ADDRESS_WILDCARD, address)
		}
		is String -> {
			insertAddress(paragraphs, index, answer)
		}
	}
}

