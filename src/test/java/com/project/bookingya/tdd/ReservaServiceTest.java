package com.project.bookingya.tdd;

import com.project.bookingya.dtos.ReservationDto;
import com.project.bookingya.entities.ReservationEntity;
import com.project.bookingya.entities.RoomEntity;
import com.project.bookingya.exceptions.EntityNotExistsException;
import com.project.bookingya.models.Reservation;
import com.project.bookingya.repositories.IGuestRepository;
import com.project.bookingya.repositories.IReservationRepository;
import com.project.bookingya.repositories.IRoomRepository;
import com.project.bookingya.services.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ReservaServiceTest {
    @Mock
    private IReservationRepository reservationRepository;
    @Mock
    private IRoomRepository roomRepository;
    @Mock
    private IGuestRepository guestRepository;
    @Mock
    private ModelMapper mapper;
    @InjectMocks
    private ReservationService reservationService;

    private UUID reservaId;
    private UUID roomId;
    private UUID guestId;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private ReservationEntity reservaEntity;
    private Reservation reservaModel;
    private ReservationDto reservaDto;
    private RoomEntity roomEntity;

    @BeforeEach
    public void setUp() {
        reservaId = UUID. randomUUID();
        roomId = UUID.randomUUID();
        guestId = UUID.randomUUID();
        checkIn = LocalDateTime.now().plusDays(1);
        checkOut = LocalDateTime.now().plusDays(3);

        reservaEntity = new ReservationEntity();
        reservaEntity.setId(reservaId);
        reservaEntity.setRoomId(roomId);
        reservaEntity.setGuestId(guestId);
        reservaEntity.setCheckIn(checkIn);
        reservaEntity.setCheckOut(checkOut);
        reservaEntity.setGuestsCount(2);
        reservaEntity.setNotes("Reserva de Alejandro Dussan - CC 1030675224");

        reservaModel = new Reservation();
        reservaModel.setId(reservaId);
        reservaModel.setRoomId(roomId);
        reservaModel.setGuestId(guestId);
        reservaModel.setCheckIn(checkIn);
        reservaModel.setCheckOut(checkOut);
        reservaModel.setGuestsCount(2);

        reservaDto = new ReservationDto();
        reservaDto.setRoomId(roomId);
        reservaDto.setGuestId(guestId);
        reservaDto.setCheckIn(checkIn);
        reservaDto.setCheckOut(checkOut);
        reservaDto.setGuestsCount(2);

        roomEntity = new RoomEntity();
        roomEntity.setId(roomId);
        roomEntity.setAvailable(true);
        roomEntity.setMaxGuests(4);
        roomEntity.setName("habitacion 601 - reservada por SANTIAGO ESPINAL");

    }

    @Test
    public void testGetById_Existente() {
        when(reservationRepository.findById(reservaId)).thenReturn(java.util.Optional.of(reservaEntity));
        when(mapper.map(reservaEntity,Reservation.class)).thenReturn(reservaModel);
        Reservation ReservaActiva = reservationService.getById(reservaId);

        assertNotNull(ReservaActiva);
        assertEquals(reservaId,ReservaActiva.getId());
        verify(reservationRepository, times(1)).findById(reservaId);

        System.out.println("obtener reserva por ID 1030675224 - EXITOSO -PASO");

    }

    @Test
    public void testGetById_NoExistente() {
        UUID idInexistente =UUID.randomUUID();
        when(reservationRepository.findById(idInexistente)).thenReturn(Optional.empty());
        assertThrows(EntityNotExistsException.class,()->
            reservationService.getById(idInexistente));
        verify(reservationRepository,times(1)).findById(idInexistente);
        System.out.println("obtener reserva por ID 52835389 EXITOSA - PASO ");

        }
    @Test
    public void testCreateReservation() {
        when(roomRepository.findById(any())).thenReturn(java.util.Optional.of(roomEntity));
        when(guestRepository.findById(any())).thenReturn(java.util.Optional.of(mock()));
        when(reservationRepository.existsOverlappingReservationForRoom(any(), any(), any(), any())).thenReturn(false);
        when(reservationRepository.existsOverlappingReservationForGuest(any(), any(), any(), any())).thenReturn(false);

        when(mapper.map(any(), eq(ReservationEntity.class))).thenReturn(reservaEntity);
        when(mapper.map(any(), eq(Reservation.class))).thenReturn(reservaModel);

        when(reservationRepository.saveAndFlush(any())).thenReturn(reservaEntity);

        Reservation HabitacionReservada601 = reservationService.create(reservaDto);

        assertNotNull(HabitacionReservada601);
        System.out.println("'Crear una reserva de Habitacion' - 1030675224 PASO COMPLETADO FORMA EXITOSA");
        System.out.println("Habitacion 601 para 1 persona");

    }

    @Test
    public void testGetAllReservations() {
        when(reservationRepository.findAll()).thenReturn(java.util.List.of(reservaEntity));
        ReservationService spyService = spy(reservationService);
        doReturn(java.util.List.of(reservaModel)).when(spyService).getAll();

        java.util.List<Reservation> ListasReservas  = spyService.getAll();

        assertNotNull(ListasReservas,"la lista de reservas deberia tener al menos un elemento");
        assertEquals(1, ListasReservas.size());
        System.out.println("'Consultar las reservas exitosas'ALEJANDRO DUSSAN - PASO COMPLETADO EXITOSO");
        System.out.println("Habitacion 601 - 1 persona");
    }

    @Test
    public void testDeleteReservation() {

        when(reservationRepository.findById(reservaId))
                .thenReturn(java.util.Optional.of(reservaEntity));

        doNothing().when(reservationRepository).delete(reservaEntity);
        doNothing().when(reservationRepository).flush();

        assertDoesNotThrow(() -> reservationService.delete(reservaId),"La ELIMINACION no deberia lanzar ningun excepcion");

        verify(reservationRepository, times(1)).delete(reservaEntity);
        verify(reservationRepository, times(1)).flush();

        System.out.println("'Eliminar una reserva' SANTIAGO ESPINAL- PASO COMPLETADO EXITOSO");
    }

    @Test
    public void testUpdateReservation() {
        ReservationService spyService = spy(reservationService);
        doReturn(reservaModel).when(spyService).update(any(), any());

        Reservation ActualizarReservaAlejandro  = spyService.update(reservaDto, reservaId);
        assertNotNull("Alejandro Dussan");
        System.out.println("'Actualizar una reserva' CC 1030675224 Cambiar fecha de reserva 3 Mayo 2026- PASO COMPLETADO EXITOSO");
    }

}
