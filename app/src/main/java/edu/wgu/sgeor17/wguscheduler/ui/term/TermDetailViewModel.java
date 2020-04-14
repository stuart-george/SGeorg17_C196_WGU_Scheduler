package edu.wgu.sgeor17.wguscheduler.ui.term;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import edu.wgu.sgeor17.wguscheduler.model.Term;
import edu.wgu.sgeor17.wguscheduler.repository.AppRepository;

public class TermDetailViewModel extends AndroidViewModel {
    private MutableLiveData<Term> termData = new MutableLiveData<>();
    private AppRepository repository;
    private Executor executor = Executors.newSingleThreadExecutor();


    public TermDetailViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(getApplication());
    }

    public void loadData(final int termID) {
        executor.execute(() -> {
            Term tempTerm = repository.getTermByID(termID);
            termData.postValue(tempTerm);
        });

    }

    public MutableLiveData<Term> getTermData() {
        return termData;
    }

    public void saveTerm (String title, Date startDate, Date endDate) {
        Term term = termData.getValue();
        if (term == null) {
            if (TextUtils.isEmpty(title.trim())) {
                return;
            }
            term = new Term(title.trim(), startDate, endDate);
        } else {
            term.setTitle(title.trim());
            term.setStartDate(startDate);
            term.setEndDate(endDate);
        }
        repository.insertTerm(term);
    }

    public void deleteTerm () {
        repository.deleteTerm(termData.getValue());
    }
}
