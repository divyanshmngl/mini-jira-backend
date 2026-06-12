-- Reference schema (aligns with JPA entity)
CREATE TABLE IF NOT EXISTS tasks (
    id              SERIAL PRIMARY KEY,
    title           VARCHAR(255) NOT NULL,
    description     TEXT,
    created_by      INTEGER NOT NULL,
    assigned_to     INTEGER,
    swimlane        VARCHAR(255) NOT NULL DEFAULT 'TODO',
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    version         BIGINT DEFAULT 0,
    CONSTRAINT tasks_swimlane_check CHECK (
        swimlane IN ('TODO', 'IN_PROGRESS', 'DONE')
    )
);

CREATE INDEX IF NOT EXISTS idx_tasks_created_by ON tasks (created_by);
CREATE INDEX IF NOT EXISTS idx_tasks_assigned_to ON tasks (assigned_to);
CREATE INDEX IF NOT EXISTS idx_tasks_swimlane ON tasks (swimlane);
CREATE INDEX IF NOT EXISTS idx_tasks_created_at ON tasks (created_at DESC);
