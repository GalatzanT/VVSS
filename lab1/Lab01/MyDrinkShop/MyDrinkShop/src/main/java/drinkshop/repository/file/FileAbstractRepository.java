package drinkshop.repository.file;

import drinkshop.repository.AbstractRepository;
import drinkshop.service.exception.PersistenceException;

import java.io.*;

public abstract class FileAbstractRepository<ID, E>
        extends AbstractRepository<ID, E> {

    protected String fileName;

    protected FileAbstractRepository(String fileName) {
        this.fileName = fileName;
        loadFromFile();
    }

    protected void loadFromFile() {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                // ensure parent directories exist
                File parent = file.getParentFile();
                if (parent != null) parent.mkdirs();
                file.createNewFile(); // create empty file
            } catch (IOException ex) {
                throw new PersistenceException("Cannot create data file: " + fileName, ex);
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            String line;
            while ((line = br.readLine()) != null) {
                E entity = extractEntity(line);
                super.save(entity);
            }

        } catch (IOException e) {
            throw new PersistenceException("Failed to load from file: " + fileName, e);
        }
    }

    private void writeToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {

            for (E entity : entities.values()) {
                bw.write(createEntityAsString(entity));
                bw.newLine();
            }

        } catch (IOException e) {
            throw new PersistenceException("Failed to write to file: " + fileName, e);
        }
    }

    @Override
    public E save(E entity) {
        E e = super.save(entity);
        writeToFile();
        return e;
    }

    @Override
    public E delete(ID id) {
        E e = super.delete(id);
        writeToFile();
        return e;
    }

    @Override
    public E update(E entity) {
        E e = super.update(entity);
        writeToFile();
        return e;
    }

    protected abstract E extractEntity(String line);

    protected abstract String createEntityAsString(E entity);
}
