package no.metatrack.server.feature.file

import no.metatrack.server.feature.project.ProjectService
import no.metatrack.server.feature.sample.Sample
import no.metatrack.server.feature.sample.SampleRepository
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.InputStream
import java.io.InputStreamReader
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

@Service
class CsvFileProcessor(
    private val projectService: ProjectService,
    private val sampleRepository: SampleRepository,
) {
    @Transactional
    fun ingestCSVSampleSheet(
        projectId: UUID,
        inputStream: InputStream,
    ) {
        val project = projectService.getProjectById(projectId) ?: throw NoSuchElementException("Project not found")

        val dateFormats =
            listOf(
                DateTimeFormatter.ISO_LOCAL_DATE,
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("ddMMyyyy"),
            )

        InputStreamReader(inputStream).use { reader ->
            val csvParser =
                CSVParser.parse(
                    reader,
                    CSVFormat.DEFAULT
                        .builder()
                        .setHeader()
                        .setSkipHeaderRecord(true)
                        .setIgnoreEmptyLines(true)
                        .setTrim(true)
                        .get(),
                )

            for ((index, record) in csvParser.records.withIndex()) {
                try {
                    val alias = record.get("alias")
                    val taxId = record.get("taxId").toInt()
                    val mlst = record.get("mlst")
                    val isolationSource = record.get("isolationSource")
                    val collectionDateRaw = record.get("collectionDate")
                    val geoLocation = record.get("geoLocation")
                    val sequencingLab = record.get("sequencingLab")
                    val institution = record.get("institution")
                    val hostHealthState = record.get("hostHealthState")
                    val collectionDate =
                        dateFormats
                            .firstNotNullOfOrNull { fmt ->
                                runCatching { LocalDate.parse(collectionDateRaw, fmt) }.getOrNull()
                            }
                            ?: throw IllegalArgumentException("Invalid date format: $collectionDateRaw")

                    val newSample =
                        Sample(
                            alias = alias,
                            taxId = taxId,
                            mlst = mlst,
                            isolationSource = isolationSource,
                            collectionDate = collectionDate,
                            geoLocation = geoLocation,
                            sequencingLab = sequencingLab,
                            institution = institution,
                            hostHealthState = hostHealthState,
                            createdOn = Instant.now(),
                            lastUpdatedOn = Instant.now(),
                            project = project,
                        )
                    sampleRepository.save(newSample)
                } catch (ex: Exception) {
                    println("Skipping record #$index due to error: ${ex.message}")
                }
            }
            csvParser.close()
        }
    }
}
