package org.honeroku.timepro.app.main.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import org.honeroku.timepro.R;
import org.honeroku.timepro.domain.constraint.AccountConstraint;
import org.honeroku.timepro.domain.entity.Account;
import org.honeroku.timepro.domain.event.LoginEvent;
import org.honeroku.timepro.domain.repository.AccountRepository;
import org.honeroku.timepro.domain.service.TimeProService;
import org.honeroku.timepro.util.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class InitActivity extends Activity {

    @Bind(R.id.timepro_domain) TextView timeproDomainView;
    @Bind(R.id.user_id)  TextView userIdView;
    @Bind(R.id.password) TextView passwordView;
    @Bind(R.id.login)    Button   loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        ButterKnife.bind(this);

        EventBus.getInstance().register(this);

        if (AccountRepository.load(this) != null) {
            startActivity(new Intent(this, AttendanceActivity.class));
            finish();
        }
    }

    @OnClick(R.id.login)
    public void onClickLogin() {
        Account account = Account.create(
                timeproDomainView.getText().toString(),
                userIdView.getText().toString(),
                passwordView.getText().toString()
        );

        AccountConstraint constraint = new AccountConstraint();
        if (!constraint.isSatisfiedBy(account)) {
            for (String message : constraint.getErrorMessages(account)) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
            return;
        }

        loginButton.setEnabled(false);

        TimeProService.login(account);
    }

    @Subscribe
    public void onLoginEvent(LoginEvent loginEvent) {
        if (loginEvent.isSuccess()) {
            Account account = loginEvent.getAccount();
            AccountRepository.save(this, account);

            startActivity(new Intent(this, AttendanceActivity.class));
            finish();
        } else if (loginEvent.hasMessage()) {
            Toast.makeText(this, loginEvent.getMessage(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "ログインに失敗しました", Toast.LENGTH_SHORT).show();
        }
        loginButton.setEnabled(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        EventBus.getInstance().unregister(this);
    }

}
