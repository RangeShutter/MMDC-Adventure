type ClassCardProps = {
  className?: string
  fields: string[]
  methods: string[]
  name: string
  stereotype?: string
}

function ClassCard({
  className = '',
  fields,
  methods,
  name,
  stereotype,
}: ClassCardProps) {
  return (
    <article className={`class-card ${className}`}>
      <header className="class-header">
        {stereotype && <span className="stereotype">«{stereotype}»</span>}
        <h2>{name}</h2>
      </header>
      <div className="class-section">
        {fields.map((field) => (
          <p key={field}>
            <span className="visibility">−</span>
            {field}
          </p>
        ))}
      </div>
      <div className="class-section methods">
        {methods.map((method) => (
          <p key={method}>
            <span className="visibility">+</span>
            {method}
          </p>
        ))}
      </div>
    </article>
  )
}

export default function App() {
  return (
    <main className="page-shell">
      <section className="intro">
        <div>
          <p className="eyebrow">SYSTEM DESIGN / 01</p>
          <h1>Dashboard application</h1>
          <p className="subtitle">
            A UML class diagram for an analytics dashboard that lets users
            compose widgets and pull live metrics.
          </p>
        </div>
        <div className="legend" aria-label="Diagram legend">
          <span>
            <i className="legend-line solid" />
            Association
          </span>
          <span>
            <i className="legend-line dashed" />
            Dependency
          </span>
          <span>
            <i className="legend-diamond" />
            Composition
          </span>
        </div>
      </section>

      <section className="diagram-frame">
        <div className="diagram-scroll">
          <div className="diagram" aria-label="UML class diagram">
            <svg
              aria-hidden="true"
              className="connectors"
              viewBox="0 0 1040 660"
            >
              <defs>
                <marker
                  id="arrow"
                  markerHeight="8"
                  markerWidth="8"
                  orient="auto"
                  refX="7"
                  refY="4"
                  viewBox="0 0 8 8"
                >
                  <path d="M0 0L8 4L0 8Z" fill="#63706a" />
                </marker>
              </defs>

              <path className="relation" d="M302 170H398" />
              <text className="multiplicity" x="315" y="159">
                1
              </text>
              <text className="multiplicity" x="374" y="159">
                0..*
              </text>
              <text className="relation-label" x="335" y="192">
                owns
              </text>

              <path className="relation" d="M640 170H736" />
              <text className="multiplicity" x="652" y="159">
                1
              </text>
              <text className="multiplicity" x="708" y="159">
                1..*
              </text>
              <text className="relation-label" x="663" y="192">
                contains
              </text>
              <path
                className="diamond"
                d="M640 170L653 162L666 170L653 178Z"
              />

              <path className="relation" d="M519 286V382" />
              <text className="multiplicity" x="529" y="306">
                1
              </text>
              <text className="multiplicity" x="529" y="368">
                1
              </text>
              <text className="relation-label" x="534" y="341">
                powered by
              </text>

              <path
                className="relation dashed"
                d="M782 286V336H640V437"
                markerEnd="url(#arrow)"
              />
              <text className="relation-label" x="668" y="326">
                fetches data
              </text>
            </svg>

            <ClassCard
              className="user"
              fields={[
                'id: UUID',
                'email: String',
                'role: UserRole',
              ]}
              methods={['login(): Session', 'createDashboard(): Dashboard']}
              name="User"
              stereotype="entity"
            />
            <ClassCard
              className="dashboard"
              fields={[
                'id: UUID',
                'title: String',
                'layout: LayoutConfig',
              ]}
              methods={['addWidget(): Widget', 'refreshAll(): void']}
              name="Dashboard"
              stereotype="aggregate"
            />
            <ClassCard
              className="widget"
              fields={[
                'id: UUID',
                'type: WidgetType',
                'position: GridPosition',
              ]}
              methods={['render(): View', 'updateConfig(): void']}
              name="Widget"
              stereotype="entity"
            />
            <ClassCard
              className="metrics"
              fields={[
                'sourceId: UUID',
                'query: MetricQuery',
                'cacheTtl: Integer',
              ]}
              methods={['fetchMetrics(): MetricSet', 'aggregate(): Summary']}
              name="MetricService"
              stereotype="service"
            />

            <div className="diagram-note">
              <span>NOTE</span>
              A user owns many dashboards. Each dashboard composes one or
              more widgets and relies on MetricService for live KPI and
              chart data.
            </div>
          </div>
        </div>
        <footer className="frame-footer">
          <span>CLASS DIAGRAM</span>
          <span>DASHBOARD DOMAIN</span>
          <span>V1.0</span>
        </footer>
      </section>
    </main>
  )
}
