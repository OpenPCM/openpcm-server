package org.openpcm.dao;

import org.openpcm.model.Facility;
import org.openpcm.model.Room;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RoomRepository extends PagingAndSortingRepository<Facility, Long> {

	Room findByName(String name);
}
