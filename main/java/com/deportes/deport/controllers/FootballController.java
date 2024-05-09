package com.deportes.deport.controllers;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.deportes.deport.services.Implements.CsvExportService;
import com.deportes.deport.services.Implements.FootballAPIService;
import com.mashape.unirest.http.exceptions.UnirestException;

import io.jsonwebtoken.io.IOException;

@RestController
@RequestMapping("/api")
public class FootballController {

    private final CsvExportService csvExportService;

    public FootballController(CsvExportService csvExportService) {
        this.csvExportService = csvExportService;
    }

    @Autowired
    private FootballAPIService service;







    // PARA VER LA ESTRUCTURA DE LOS DATOS QUE ME PASA EL JSON


    @Autowired
    private FootballAPIService footballService;

    @GetMapping("/footballLeagues")
    public ResponseEntity<String> getFootballLeagues() {
        String leaguesResponse = footballService.getFootballLeagues();

        if (leaguesResponse != null) {
            return ResponseEntity.ok(leaguesResponse);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener las ligas de fútbol");
        }
    }

    @GetMapping("/export/football-leagues")
    public String exportFootballLeaguesToCsv() {
        String filePath = "D:/ProyectosWebJava/deport/excells/football_leagues.csv";
        csvExportService.exportFootballLeaguesToCsv(filePath);
        return "Los datos se han exportado correctamente a " + filePath;
    }

    @GetMapping("/export/timezones")
    public String exportTimezonesToCsv() {
        String filePath = "D:/ProyectosWebJava/deport/excells/timezones.csv";
        csvExportService.exportTimezonesToCsv(filePath);
        return "Los datos de las zonas horarias se han exportado correctamente a " + filePath;
    }

    @GetMapping("/export/countries")
    public String exportCountriesToCsv() {
        String filePath = "D:/ProyectosWebJava/deport/excells/countries.csv";
        csvExportService.exportCountriesToCsv(filePath);
        return "Los datos de los países se han exportado correctamente a " + filePath;
    }

    @GetMapping("/export/seasons")
    public String exportLeaguesSeasonsToCsv() {
        String filePath = "D:/ProyectosWebJava/deport/excells/leagues_seasons.csv";
        csvExportService.exportLeaguesSeasonsToCsv(filePath);
        return "Los datos de las temporadas de la liga se han exportado correctamente a " + filePath;
    }

    // pasar el nombre de la liga y la sesion o temporada para obtener todos los equipos de esa session
    @GetMapping("/export/teams/{league}/{season}")
    public String exportTeamsToCsv(@PathVariable String league, @PathVariable String season) {
        String filePath = "D:/ProyectosWebJava/deport/excells/teams.csv";
        try {
            csvExportService.exportTeamsToCsv(filePath, league, season);
        } catch (UnirestException e) {
            e.printStackTrace();
            return "Error al exportar los datos de los equipos";
        }
        return "Los datos de los equipos se han exportado correctamente a " + filePath;
    }

    @GetMapping(value = "/{leagueName}/{teamName}/{season}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> exportTeamStatisticsToCsvController(
            @PathVariable String leagueName,
            @PathVariable String teamName,
            @PathVariable String season) {
        try {
            // Llamar al método exportTeamStatisticsToCsv
            String filePath = "D:/ProyectosWebJava/deport/excells/team_statistics.csv";
            CsvExportService.exportTeamStatisticsToCsv(filePath, leagueName, teamName, season);

            return ResponseEntity.ok("Los datos de estadísticas del equipo se han exportado correctamente a " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al exportar los datos de estadísticas del equipo: " + e.getMessage());
        }
    }

    // REVISAR EL FORMATO DE LOS DATOS Y  SUS TIPOS TEAMS STATICS
    @GetMapping("/team/statistics/{leagueId}/{teamId}/{season}")
    public JSONObject getTeamStatisticsJsonController(
            @PathVariable String leagueId,
            @PathVariable String teamId,
            @PathVariable String season) {

        // Llamada al método que obtiene las estadísticas del equipo en formato JSON
        JSONObject teamStatisticsJson = service.getTeamStatisticsJson(leagueId, teamId, season);

        System.out.println(teamStatisticsJson);
        // Devolución de las estadísticas del equipo en formato JSON
        return teamStatisticsJson;
    }


    // Team por nombre
    @GetMapping("/find-team-id/{teamName}")
    public String findTeamId(@PathVariable String teamName) {
        Integer teamId = CsvExportService.findTeamIdByName(teamName);
        if (!teamId.equals(0)) {
            return "El ID del equipo '" + teamName + "' es: " + teamId;
        } else {
            return "No se encontró ningún equipo con el nombre '" + teamName + "'";
        }
    }

    // Venue o estadios

    @GetMapping("/venues/{country}")
    public ResponseEntity<String> exportVenuesToCsv(@PathVariable String country) {
        String filePath = "D:/ProyectosWebJava/deport/excells/Estadios.csv";
        CsvExportService.fetchAndSaveVenues(country, filePath);
        return ResponseEntity.ok("Los datos de venues se han exportado correctamente a " + filePath);
    }





    // STADING CLASIFICACIONES PARA HACER PRIN EN CONSOLA

    @GetMapping("/standings/print/{leagueId}/{season}")
    public void fetchAndPrintStandings(
            @PathVariable int leagueId,
            @PathVariable int season) {
        csvExportService.fetchAndPrintStandings(leagueId, season);
    }

    // Guardar en archivo csv

    @GetMapping("/standings/csv/{leagueName:.+}/{season}")
    public ResponseEntity<String> saveStandings(@PathVariable("leagueName") String leagueName, @PathVariable("season") int season) {
        try {
            String filePath = "D:/ProyectosWebJava/deport/excells/Clasificaciones.csv";
            String leaguesFilePath = "D:/ProyectosWebJava/deport/excells/football_leagues.csv";
            System.out.println(leagueName);
            int leagueId = CsvExportService.getLeagueId(leagueName, leaguesFilePath);
            System.out.println(leagueId);
            if (leagueId == -1) {
                return new ResponseEntity<>("Error al guardar los datos", HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                CsvExportService.fetchAndSaveStandings(leagueId, season, filePath);
                return new ResponseEntity<>("Datos guardados en el archivo CSV: " + filePath, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error al guardar los datos", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    // ROUNDS por consola

    @GetMapping("/rounds/print/{leagueId}/{season}")
    public void fetchAndPrintRounds(
            @PathVariable int leagueId,
            @PathVariable int season) throws UnirestException {
        CsvExportService.fetchRounds(leagueId, season);
    }


    // ROUNDS save csv


    @GetMapping("/rounds/csv/{leagueName:.+}/{season}")
    public ResponseEntity<String> saveCsvRounds
        ( @PathVariable("leagueName") String leagueName, @PathVariable("season") int season) throws UnirestException {
        try {
            String filePath = "D:/ProyectosWebJava/deport/excells/Rounds.csv";
            String leaguesFilePath = "D:/ProyectosWebJava/deport/excells/football_leagues.csv";
            System.out.println(leagueName);
            int leagueId = CsvExportService.getLeagueId(leagueName, leaguesFilePath);
            System.out.println(leagueId);
            if (leagueId == -1) {
                return new ResponseEntity<>("Error al guardar los datos", HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                CsvExportService.fetchRounds(leagueId, season, filePath);
                return new ResponseEntity<>("Datos guardados en el archivo CSV: " + filePath, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error al guardar los datos", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Fixtures calendario por consola

    @GetMapping("/fixture/print/{leagueId}/{season}")
    public void fetchAndPrintfixture(
            @PathVariable int leagueId,
            @PathVariable int season) throws UnirestException {
        CsvExportService.fetchFixtures(leagueId, season);
    }


    // Fixtures calendario por csv



    @GetMapping("/fixture/csv/{leagueName:.+}/{season}")
    public ResponseEntity<String> saveCsvAllFixtures
        ( @PathVariable("leagueName") String leagueName, @PathVariable("season") int season) throws UnirestException {
        try {
            String filePath = "D:/ProyectosWebJava/deport/excells/PartidosAll.csv";
            String leaguesFilePath = "D:/ProyectosWebJava/deport/excells/football_leagues.csv";
            System.out.println(leagueName);
            int leagueId = CsvExportService.getLeagueId(leagueName, leaguesFilePath);
            System.out.println(leagueId);
            if (leagueId == -1) {
                return new ResponseEntity<>("Error al guardar los datos", HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                CsvExportService.fetchAndSaveFixtures(leagueId, season, filePath);
                return new ResponseEntity<>("Datos guardados en el archivo CSV: " + filePath, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error al guardar los datos", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



        //  Partidos por id

            // USAR LA URL DE PARTIDO POR ID PARA QUE ME DE TODA LA INFO VALIOSA DEL PARTIDO Y LOS EQUIPOS
            // SOLO QUE PRIMERO SE BUSCA TODOS LOS PARTIDOS DE LA TEMPORADA PARA POSTERIOR MENTE CON ESOS
            // PARTIDOS ELEGIR EL PARTIDO A BUSCAR POR MEDIO DE SU ID


            // imprimir por pantalla

            @GetMapping("/partidoxid/print/{partidoId}")
            public void fetchAndPrintfixture( @PathVariable String partidoId ) throws UnirestException {
                CsvExportService.fetchFixtureById(partidoId);
            }

            // save csv


            @GetMapping("/partidoxid/csv/{partidoId}")
            public ResponseEntity<String> partidoxid(@PathVariable("partidoId") String partidoId) {
                String filePath = "D:/ProyectosWebJava/deport/excells/PartidoXId.csv";
                CsvExportService.fetchAndSaveFixturesById(partidoId, filePath);
                return ResponseEntity.status(HttpStatus.OK).body("Datos del partido guardados en el archivo CSV: " + filePath);
            }


    // Partidos Ya jugados info


            // imprimir por pantalla

            @GetMapping("/partidoxequiposid/print/{h2h}")
            public void fetchHeadToHead(@PathVariable String h2h) throws UnirestException {
                // Dividir la cadena por el guion para obtener los IDs de equipo
                String[] teamIds = h2h.split("-");
                int teamId1 = Integer.parseInt(teamIds[0]);
                int teamId2 = Integer.parseInt(teamIds[1]);

                // Luego, puedes utilizar los IDs de equipo según sea necesario
                CsvExportService.fetchHeadToHead(teamId1, teamId2);
            }

            // save csv

            @GetMapping("/fetchAndSaveHeadToHead/{teamId1}/{teamId2}")
            public ResponseEntity<String> fetchAndSaveHeadToHead(@PathVariable int teamId1, @PathVariable int teamId2) {
                try {
                    String filePath = "D:/ProyectosWebJava/deport/excells/PartidoYaJugados.csv";
                    // Llamar al método para obtener y guardar los datos
                    CsvExportService.fetchAndSaveHeadToHead(teamId1, teamId2, filePath);
                    // Devolver una respuesta con estado OK si todo fue exitoso
                    return ResponseEntity.ok("Datos guardados en el archivo CSV: " + filePath);
                } catch (IllegalArgumentException e) {
                    // Capturar excepción si los parámetros son inválidos
                    return ResponseEntity.badRequest().body("Los IDs de equipo proporcionados no son válidos.");
                } catch (Exception e) {
                    // Capturar excepción general
                    e.printStackTrace();
                    // Devolver una respuesta con estado de error interno del servidor (500)
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud.");
                }
            }


    // Statistics Estadisticas de partido especificado por su id ( funciona como la anterior con el id del partido )

            // imprimir pantalla

            @GetMapping("/fixtures/{fixtureId}/statistics")
            public void fetchFixtureStatistics(@PathVariable int fixtureId) {
                CsvExportService.fetchFixtureStatistics(fixtureId);
            }



            // save csv


            @GetMapping("/fetchAndSaveFixtureStatistics/{fixtureId}")
            public ResponseEntity<String> fetchAndSaveFixtureStatistics(@PathVariable String fixtureId) throws UnirestException, java.io.IOException {
                try {
                    String filePath = "D:/ProyectosWebJava/deport/excells/EstadisticadePartidoporId.csv";
                    // Llamar al método para obtener y guardar los datos
                    CsvExportService.fetchAndSaveFixtureStatistics(fixtureId, filePath);
                    // Devolver una respuesta con estado OK si todo fue exitoso
                    return ResponseEntity.ok("Datos guardados en el archivo CSV: " + filePath);
                } catch (IOException e) {
                    // Capturar excepción si hay un error con la solicitud HTTP o de E/S al escribir en el archivo CSV
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud.");
                }
            }


    // Events

    // URL: https://v3.football.api-sports.io/fixtures/events?fixture=215662
    // Esta solicitud devuelve todos los eventos disponibles para el partido con el ID de fixture 215662.
    // Los eventos pueden incluir goles, tarjetas, sustituciones, faltas, entre otros.


    // imprimir pantalla

    @GetMapping("/events/{fixtureId}")
            public void fetchFixtureEvents(@PathVariable int fixtureId) {
                CsvExportService.fetchFixtureStatistics(fixtureId);
            }




    // save csv

    @GetMapping("/events/csv/{fixtureId}")
    public ResponseEntity<String> Events(@PathVariable int fixtureId) throws java.io.IOException {
        try {
            String filePath = "D:/ProyectosWebJava/deport/excells/EventsxFixtureId.csv";

            // Crear una instancia de CsvExportService
            CsvExportService csvExportService = new CsvExportService();

            // Llamar al método para obtener y guardar los datos
            csvExportService.fetchAndSaveFixtureEvents(fixtureId, filePath);

            // Devolver una respuesta con estado OK si todo fue exitoso
            return ResponseEntity.ok("Datos guardados en el archivo CSV: " + filePath);
        } catch (IOException e) {
            // Capturar excepción si hay un error con la solicitud HTTP o de E/S al escribir en el archivo CSV
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud.");
        }
    }

        //     Lineups

        // https://v3.football.api-sports.io/fixtures/lineups?fixture=592872: Esta URL devuelve todas las
        // alineaciones disponibles para el partido con el ID de fixture 592872. Proporciona información
        // sobre los jugadores alineados en ambos equipos

    // imprimir pantalla

    @GetMapping("/LineUps/print/{fixtureId}")
    public void fetchFixtureLineups(@PathVariable int fixtureId) {
        CsvExportService.fetchFixtureLineups(fixtureId);
    }

    // save csv

    @GetMapping("/lineups/csv/{fixtureId}")
        public ResponseEntity<String> Lineups(@PathVariable int fixtureId) throws java.io.IOException {
            try {
                String filePath = "D:/ProyectosWebJava/deport/excells/LineupsforIdFixte.csv";

                // Crear una instancia de CsvExportService
                CsvExportService csvExportService = new CsvExportService();

                // Llamar al método para obtener y guardar los datos
                csvExportService.fetchAndSaveLineups(fixtureId, filePath);

                // Devolver una respuesta con estado OK si todo fue exitoso
                return ResponseEntity.ok("Datos guardados en el archivo CSV: " + filePath);
            } catch (IOException e) {
                // Capturar excepción si hay un error con la solicitud HTTP o de E/S al escribir en el archivo CSV
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud.");
            }
        }


    //  Players statistics


        // imprimir pantalla


        @GetMapping("/playersStadistic/print/{fixtureId}/{teamId}")
        public void fetchFixturePlayers(@PathVariable int fixtureId, @PathVariable int teamId) {
            CsvExportService.fetchFixturePlayers(fixtureId, teamId);
        }


        // save csv

        @GetMapping("/playersStadistic/csv/{fixtureId}/{teamId}")
        public ResponseEntity<String> PlayersStadistic(@PathVariable int fixtureId,@PathVariable int teamId) throws java.io.IOException {
            try {
                String filePath = "D:/ProyectosWebJava/deport/excells/PlayersStadistic.csv";

                // Crear una instancia de CsvExportService
                CsvExportService csvExportService = new CsvExportService();

                // Llamar al método para obtener y guardar los datos
                csvExportService.fetchAndSavePlayersData( fixtureId,  teamId, filePath);

                // Devolver una respuesta con estado OK si todo fue exitoso
                return ResponseEntity.ok("Datos guardados en el archivo CSV: " + filePath);
            } catch (IOException e) {
                // Capturar excepción si hay un error con la solicitud HTTP o de E/S al escribir en el archivo CSV
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud.");
            }
        }

    // Injuries - Lesiones

    // https://v3.football.api-sports.io/injuries?league=2&season=2020: Proporciona todas las lesiones
    // disponibles para una liga específica y una temporada específica.

        // imprimir pantalla

        @GetMapping("/injuries/print/{leagueId}/{seasonYear}")
        public void fetchInjuries(@PathVariable int leagueId, @PathVariable int seasonYear) {
            CsvExportService.fetchInjuries(leagueId, seasonYear);
        }

        // save csv

        @GetMapping("/injuries/csv/{leagueId}/{seasonYear}")
        public ResponseEntity<String> Injuries(@PathVariable int leagueId, @PathVariable int seasonYear) throws java.io.IOException, UnirestException {
            try {
                String filePath = "D:/ProyectosWebJava/deport/excells/lesiones.csv";

                // Crear una instancia de CsvExportService
                CsvExportService csvExportService = new CsvExportService();

                // Llamar al método para obtener y guardar los datos
                csvExportService.fetchAndSaveInjuries( leagueId,  seasonYear, filePath);

                // Devolver una respuesta con estado OK si todo fue exitoso
                return ResponseEntity.ok("Datos guardados en el archivo CSV: " + filePath);
            } catch (IOException e) {
                // Capturar excepción si hay un error con la solicitud HTTP o de E/S al escribir en el archivo CSV
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud.");
            }
        }




    // Top Scorers

        // imprimir pantalla


        // save csv




        

}
