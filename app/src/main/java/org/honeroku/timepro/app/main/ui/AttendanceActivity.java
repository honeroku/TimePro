package org.honeroku.timepro.app.main.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import org.honeroku.timepro.R;
import org.honeroku.timepro.domain.repository.AccountRepository;
import org.honeroku.timepro.domain.service.TimeProService;
import org.honeroku.timepro.domain.event.ClockInEvent;
import org.honeroku.timepro.domain.event.ClockOutEvent;
import org.honeroku.timepro.util.EventBus;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AttendanceActivity extends Activity {

    @InjectView(R.id.clock_in)  Button clockInButton;
    @InjectView(R.id.clock_out) Button clockOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        ButterKnife.inject(this);

        EventBus.getInstance().register(this);

        if (AccountRepository.load(this) == null) {
            startActivity(new Intent(this, InitActivity.class));
            finish();
        }
    }

    @OnClick(R.id.clock_in)
    public void onClickClockIn() {
        setEnabled(false);

        TimeProService.clockIn(AccountRepository.load(this));
    }

    @OnClick(R.id.clock_out)
    public void onClickClockOut() {
        setEnabled(false);

        TimeProService.clockOut(AccountRepository.load(this));
    }

    @Subscribe
    public void onClockInEvent(ClockInEvent clockInEvent) {
        setEnabled(true);

        if (clockInEvent.isSuccess()) {
            Toast.makeText(this, "出勤記録を付けました", Toast.LENGTH_SHORT).show();
        } else if (clockInEvent.hasMessage()) {
            Toast.makeText(this, clockInEvent.getMessage(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "出勤記録に失敗しました", Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void onClockOutEvent(ClockOutEvent clockOutEvent) {
        setEnabled(true);

        if (clockOutEvent.isSuccess()) {
            Toast.makeText(this, "退勤記録を付けました", Toast.LENGTH_SHORT).show();
        } else if (clockOutEvent.hasMessage()) {
            Toast.makeText(this, clockOutEvent.getMessage(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "退勤記録に失敗しました", Toast.LENGTH_SHORT).show();
        }
    }

    private void setEnabled(boolean enabled) {
        clockInButton.setEnabled(enabled);
        clockOutButton.setEnabled(enabled);
    }

}
