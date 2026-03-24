# Selenium automation (Java + TestNG)

## Run locally

```bash
mvn clean test -Denv=dev -Dheadless=true -Dbrowser=chrome
```

## Profiles

- `-Denv=dev|staging|prod` loads `src/main/resources/config/{env}.properties` after `default.properties`.
- Remote Grid: set `SELENIUM_GRID_URL` or `-Dselenium.grid.url=http://hub:4444/wd/hub`.

## Suites

| File | Purpose |
|------|---------|
| `src/test/resources/suites/smoke.xml` | Sequential smoke (`groups=smoke`) |
| `src/test/resources/suites/parallel-regression.xml` | Parallel methods + `regression` group (use with Grid) |

## Reports

- HTML: `target/extent-report/index.html`
- Failed-run screenshots: `screenshots/` (configurable)

## CI

- GitHub Actions: `.github/workflows/ui-tests.yml`
- GitLab: `.gitlab-ci.yml` (Maven + `selenium/standalone-chrome` service)
- Jenkins: `Jenkinsfile`
