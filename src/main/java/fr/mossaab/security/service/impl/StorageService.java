package fr.mossaab.security.service.impl;

import fr.mossaab.security.config.PathConfig;
import fr.mossaab.security.entities.FileData;
import fr.mossaab.security.entities.User;
import fr.mossaab.security.repository.FileDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для работы с хранилищем файлов.
 */
@Service
public class StorageService {

    @Autowired
    private FileDataRepository fileDataRepository;

    @Autowired
    private PathConfig pathConfig;
    /**
     * Загружает изображение пользователя в файловую систему.
     *
     * @param file Загружаемый файл
     * @param user Пользователь, которому принадлежит изображение
     * @return Данные о загруженном файле
     * @throws IOException Если происходит ошибка ввода-вывода при загрузке файла
     */
    public FileData uploadImageToFileSystemAvatarUser(MultipartFile file, User user) throws IOException {
        String fileName = UUID.randomUUID().toString();
        String filePath = pathConfig.getStorageServiceFolderPath() + fileName + ".png";

        // Удалить старый объект FileData, если он существует
        if (user.getFileData() != null) {
            fileDataRepository.delete(user.getFileData());
        }

        // Создать новый объект FileData и установить пользователя перед сохранением
        FileData fileData = FileData.builder()
                .name(fileName + ".png")
                .type(file.getContentType())
                .filePath(filePath)
                .user(user)
                .build();

        // Сохранить объект FileData в репозитории
        fileData = fileDataRepository.save(fileData);

        // Перенести файл
        file.transferTo(new File(filePath));

        return fileData;
    }


    /**
     * Загружает изображение из файловой системы.
     *
     * @param fileName Имя файла для загрузки
     * @return Байтовый массив изображения
     * @throws IOException Если происходит ошибка ввода-вывода при чтении файла
     */
    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        Optional<FileData> fileData = fileDataRepository.findByName(fileName);
        String filePath=fileData.get().getFilePath();
        return Files.readAllBytes(new File(filePath).toPath());
    }
}
