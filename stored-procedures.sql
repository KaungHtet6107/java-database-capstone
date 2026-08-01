mysql> DELIMITER $
mysql>
mysql> CREATE PROCEDURE GetDailyAppointmentReportByDoctor(
    ->     IN report_date DATE
        -> )
    -> BEGIN
    ->     SELECT
                      ->         d.name AS doctor_name,
                      ->         a.appointment_time,
                      ->         a.status,
                      ->         p.name AS patient_name,
                      ->         p.phone AS patient_phone
                      ->     FROM
                      ->         appointment a
                      ->     JOIN
                      ->         doctor d ON a.doctor_id = d.id
                      ->     JOIN
                      ->         patient p ON a.patient_id = p.id
                      ->     WHERE
                      ->         DATE(a.appointment_time) = report_date
                      ->     ORDER BY
                      ->         d.name, a.appointment_time;
-> END$
Query OK, 0 rows affected (0.006 sec)

mysql>
mysql> DELIMITER ;
mysql>
mysql> CALL GetDailyAppointmentReportByDoctor('2025-04-15');
+------------------+----------------------------+--------+----------------+---------------+
| doctor_name      | appointment_time           | status | patient_name   | patient_phone |
+------------------+----------------------------+--------+----------------+---------------+
| Dr. Ava Hall     | 2025-04-15 11:00:00.000000 |      1 | Lucas Turner   | 889-666-6666  |
| Dr. Mark Johnson | 2025-04-15 12:00:00.000000 |      1 | Michael Jordan | 888-444-4444  |
| Dr. Mark Johnson | 2025-04-15 13:00:00.000000 |      1 | Olivia Moon    | 888-555-5555  |
+------------------+----------------------------+--------+----------------+---------------+
3 rows in set (0.002 sec)

Query OK, 0 rows affected (0.002 sec)

mysql> DELIMITER $
mysql>
mysql> CREATE PROCEDURE GetDoctorWithMostPatientsByMonth(
    ->     IN input_month INT,
    ->     IN input_year INT
    -> )
    -> BEGIN
    ->     SELECT
                      ->         doctor_id,
                      ->         COUNT(patient_id) AS patients_seen
                      ->     FROM
                      ->         appointment
                      ->     WHERE
                      ->         MONTH(appointment_time) = input_month
                      ->         AND YEAR(appointment_time) = input_year
                      ->     GROUP BY
                      ->         doctor_id
                      ->     ORDER BY
                      ->         patients_seen DESC
                      ->     LIMIT 1;
-> END $
Query OK, 0 rows affected (0.003 sec)

mysql>
mysql> DELIMITER ;
mysql>
mysql> CALL GetDoctorWithMostPatientsByMonth(4, 2025);
+-----------+---------------+
| doctor_id | patients_seen |
+-----------+---------------+
|         2 |            31 |
+-----------+---------------+
1 row in set (0.002 sec)

Query OK, 0 rows affected (0.002 sec)

mysql> DELIMITER $
mysql>
mysql> CREATE PROCEDURE GetDoctorWithMostPatientsByYear(
    ->     IN input_year INT
        -> )
    -> BEGIN
    ->     SELECT
                      ->         doctor_id,
                      ->         COUNT(patient_id) AS patients_seen
                      ->     FROM
                      ->         appointment
                      ->     WHERE
                      ->         YEAR(appointment_time) = input_year
                      ->     GROUP BY
                      ->         doctor_id
                      ->     ORDER BY
                      ->         patients_seen DESC
                      ->     LIMIT 1;
-> END $
Query OK, 0 rows affected (0.002 sec)

mysql>
mysql> DELIMITER ;
mysql>
mysql> CALL GetDoctorWithMostPatientsByYear(2025);
+-----------+---------------+
| doctor_id | patients_seen |
+-----------+---------------+
|         1 |            34 |
+-----------+---------------+
1 row in set (0.001 sec)

Query OK, 0 rows affected (0.001 sec)

mysql>