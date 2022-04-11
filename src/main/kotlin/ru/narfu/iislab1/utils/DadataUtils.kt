package ru.narfu.iislab1.utils

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForObject

object Dadata {
	private const val ADDRESS_URI = "https://suggestions.dadata.ru/suggestions/api/4_1/rs/suggest/address"
	private const val COMPANY_URI = "https://suggestions.dadata.ru/suggestions/api/4_1/rs/suggest/party"

	private val restTemp: RestTemplate = RestTemplateBuilder()
		.defaultHeader("Content-Type", "application/json")
		.defaultHeader("Accept", "application/json")
		.defaultHeader("Authorization", "Token f813ab8dadd3e74d3b417eb8b93f1503e0f00644")
		.build()

	fun getAddresses(addressTemplate: String): List<DadataAddress> =
		restTemp.postForObject<DadataResponse>(
			ADDRESS_URI,
			DadataRequest(addressTemplate)
		).toAddresses()

	fun getCompanies(companyName: String): List<DadataCompany> =
		restTemp.postForObject<DadataResponse>(
			COMPANY_URI,
			DadataRequest(companyName)
		).toCompanies()
}

private data class DadataRequest(
	val query: String,
)

private data class DadataResponse(
	val suggestions: List<Suggestion>,
) {
	data class Suggestion(
		val value: String,
		val unrestricted_value: String,
		val data: Data,
	) {
		data class Data(
			val inn: Long?,
			val ogrn: Long?,
			val okato: Long?,
			val oktmo: Long?,
			val okpo: Long?,
		)
	}
}

data class DadataAddress(
	val shortAddress: String,
	val fullAddress: String,
)

data class DadataCompany(
	val fullName: String,
	val inn: Long?,
	val ogrn: Long?,
	val okato: Long?,
	val oktmo: Long?,
	val okpo: Long?,
) {
	override fun toString(): String =
		"«$fullName» (ИНН: $inn, ОГРН: $ogrn, Код ОКАТО: $okato, Код ОКТМО: $oktmo, Код ОКПО: $okpo)"

}

private fun DadataResponse.toAddresses(): List<DadataAddress> = this.suggestions.map {
	DadataAddress(
		shortAddress = it.value,
		fullAddress = it.unrestricted_value
	)
}

private fun DadataResponse.toCompanies(): List<DadataCompany> = this.suggestions.map {
	DadataCompany(
		fullName = it.value,
		inn = it.data.inn,
		ogrn = it.data.ogrn,
		okato = it.data.okato,
		oktmo = it.data.oktmo,
		okpo = it.data.okpo
	)
}