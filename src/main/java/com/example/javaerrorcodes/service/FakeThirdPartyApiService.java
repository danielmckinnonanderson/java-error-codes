package com.example.javaerrorcodes.service;

import com.example.javaerrorcodes.form.ProcessFormReq;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class FakeThirdPartyApiService {

    private int counter = 0;

    /**
     * Pretend like we're requesting an API.
     *
     * Their API is terrible and unstable, so this method will return ResponseEntities with
     *   4XX / 5XX codes all the time.
     *
     * @return a ResponseEntity indicated either simulated success or a simulated error.
     */
    public ResponseEntity<Object> postForm(ProcessFormReq value) {
        this.counter++;
        return new ResponseEntity<Object>(this.getNextStatusCode(this.counter));
    }

    private HttpStatus getNextStatusCode(int count) {
        if (count % 7 == 0)
            return HttpStatus.BAD_REQUEST;
        if (count % 3 == 0)
            return HttpStatus.INTERNAL_SERVER_ERROR;
        if (count == 6)
            return HttpStatus.SERVICE_UNAVAILABLE;
        if (count == 11)
            return HttpStatus.GATEWAY_TIMEOUT;

        return HttpStatus.ACCEPTED;
    }
}
