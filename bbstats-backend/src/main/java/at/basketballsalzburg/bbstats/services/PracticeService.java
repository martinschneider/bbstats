package at.basketballsalzburg.bbstats.services;

import java.util.Date;
import java.util.List;

import at.basketballsalzburg.bbstats.dto.PracticeDTO;

public interface PracticeService {

	public abstract void save(PracticeDTO practice);

	public abstract List<PracticeDTO> findAll();

	public abstract List<PracticeDTO> findAll(Date dateFrom, Date dateTo);

	public abstract PracticeDTO findByName(String name);

	public abstract PracticeDTO findById(Long id);

	public abstract void delete(PracticeDTO practice);

}