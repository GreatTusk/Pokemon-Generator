/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Storage;

/**
 *
 * @author f_776
 */
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class PokemonFileUtility {

    private static final String FILE_NAME = "storedPokemon.txt";
    private static final String DATA_DIRECTORY = "Data";

    private PokemonFileUtility() {
        // Private constructor to prevent instantiation
    }

    private static Path getPokemonFilePath() {
        return Paths.get(System.getProperty("user.dir"), DATA_DIRECTORY, FILE_NAME);
    }

    public static void writePokemonData(String data) {
        try {
            Path pokemonFilePath = getPokemonFilePath();
            Files.write(pokemonFilePath, (data + "\n").getBytes(), StandardOpenOption.APPEND);
            //System.out.println("Data appended to " + FILE_NAME + " successfully.");
        } catch (IOException e) {
            // Handle the exception as needed
        }
    }

    public static List<String> readPokemonData() {
        try {
            Path pokemonFilePath = getPokemonFilePath();
            List<String> lines = Files.readAllLines(pokemonFilePath);

            // Process lines to split into individual Pokémon entries
            List<String> pokemonEntries = new ArrayList<>();
            StringBuilder currentEntry = new StringBuilder();

            for (String line : lines) {
                if (line.trim().contains("@")) {
                    // Start of a new Pokémon entry
                    if (currentEntry.length() > 0) {
                        // Add the previous entry to the list
                        pokemonEntries.add(currentEntry.toString());
                        currentEntry = new StringBuilder();
                    }
                }
                // Append the current line to the entry
                currentEntry.append(line).append("\n");
            }

            // Add the last entry to the list
            if (currentEntry.length() > 0) {
                pokemonEntries.add(currentEntry.toString());
            }

            return pokemonEntries;
        } catch (IOException e) {
            // Handle the exception as needed
            return null;
        }
    }

    public static void deletePokemonData(String partialDataToDelete) {
        try {
            Path pokemonFilePath = getPokemonFilePath();
            List<String> lines = Files.readAllLines(pokemonFilePath);

            // Process lines to split into individual Pokémon entries
            List<String> pokemonEntries = new ArrayList<>();
            StringBuilder currentEntry = new StringBuilder();

            for (String line : lines) {
                if (line.trim().contains("@")) {
                    // Start of a new Pokémon entry
                    if (currentEntry.length() > 0) {
                        // Add the previous entry to the list
                        pokemonEntries.add(currentEntry.toString());
                        currentEntry = new StringBuilder();
                    }
                }
                // Append the current line to the entry
                currentEntry.append(line).append("\n");
            }

            // Add the last entry to the list
            if (currentEntry.length() > 0) {
                pokemonEntries.add(currentEntry.toString());
            }

            // Delete entries that contain the specified data partially
            pokemonEntries.removeIf(entry -> entry.contains(partialDataToDelete));

            // Write the modified entries back to the file
            Files.write(pokemonFilePath, String.join("", pokemonEntries).getBytes());

        } catch (IOException e) {
            // Handle the exception as needed

        }
    }

    public static void updatePokemonData(String originalData, String dataToReplace) {
        try {
            Path pokemonFilePath = getPokemonFilePath();
            List<String> lines = Files.readAllLines(pokemonFilePath);

            // Process lines to split into individual Pokémon entries
            List<String> pokemonEntries = new ArrayList<>();
            StringBuilder currentEntry = new StringBuilder();

            for (String line : lines) {
                if (line.trim().contains("@")) {
                    // Start of a new Pokémon entry
                    if (currentEntry.length() > 0) {
                        // Add the previous entry to the list
                        pokemonEntries.add(currentEntry.toString());
                        currentEntry = new StringBuilder();
                    }
                }
                // Append the current line to the entry
                currentEntry.append(line).append("\n");
            }

            // Add the last entry to the list
            if (currentEntry.length() > 0) {
                pokemonEntries.add(currentEntry.toString());
            }

            // Update the entry that contains the specified data
            for (int i = 0; i < pokemonEntries.size(); i++) {
                if (pokemonEntries.get(i).contains(originalData)) {
                    pokemonEntries.set(i, dataToReplace + "\n");
                }
            }

            // Write the modified entries back to the file
            Files.write(pokemonFilePath, String.join("", pokemonEntries).getBytes());

        } catch (IOException e) {
            // Handle the exception as needed

        }
    }

}
